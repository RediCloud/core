package net.dustrean.api.network

import net.dustrean.api.redis.IRedisConnection
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RMap
import java.util.*

open class NetworkComponentManager(redisConnection: IRedisConnection) : INetworkComponentManager {

    override val networkComponents: RMap<String, NetworkComponentInfo> =
        redisConnection.getRedissonClient().getLocalCachedMap(
            "networkComponents",
            LocalCachedMapOptions.defaults<String?, NetworkComponentInfo?>()
                .storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
        )

    override suspend fun getComponentInfo(key: String) = networkComponents[key]

    override suspend fun getComponentInfo(identifier: UUID): NetworkComponentInfo? =
        getComponentInfos().find { it.identifier == identifier }

    override suspend fun getComponentInfos(type: NetworkComponentType): List<NetworkComponentInfo> =
        getComponentInfos().filter { it.type == type }

    override suspend fun getComponentInfos(): List<NetworkComponentInfo> =
        networkComponents.readAllValues().toList()

}