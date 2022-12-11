package net.dustrean.api.network

import net.dustrean.api.redis.IRedisConnection
import net.dustrean.api.tasks.futures.FutureAction
import org.redisson.api.RMap
import java.util.*

open class NetworkComponentManager(redisConnection: IRedisConnection) : INetworkComponentManager {

    override val networkComponents: RMap<String, NetworkComponentInfo> =
        redisConnection.getRedissonClient().getMapCache("networkComponents")

    override fun getComponentInfo(key: String): FutureAction<NetworkComponentInfo> {
        return FutureAction(networkComponents.getAsync(key))
    }

    override fun getComponentInfo(uniqueId: UUID): FutureAction<NetworkComponentInfo> {
        val future = FutureAction<NetworkComponentInfo>()
        getComponentInfos().whenComplete{ networkComponentInfos, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            val networkComponentInfo = networkComponentInfos.find { it.identifier == uniqueId }
            if (networkComponentInfo != null) {
                future.complete(networkComponentInfo)
                return@whenComplete
            }
            future.completeExceptionally(NoSuchElementException("No NetworkComponentInfo with the UUID $uniqueId was found"))
        }
        return future
    }

    override fun getComponentInfos(type: NetworkComponentType): FutureAction<List<NetworkComponentInfo>> {
        return getComponentInfos().map { list -> list.filter { it.type == type } }
    }

    override fun getComponentInfos(): FutureAction<List<NetworkComponentInfo>> {
        return FutureAction(networkComponents.readAllValuesAsync()).map { it.toList() }
    }

}