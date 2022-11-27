package net.dustrean.api.network

import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

open class NetworkComponentManager : INetworkComponentManager{

    private val networkComponents = mutableListOf<NetworkComponentInfo>()

    override fun getComponentInfo(key: String): NetworkComponentInfo? {
        if(networkComponents.parallelStream().filter { it.getKey() == key }.count() > 1) return null
        return networkComponents.first { it.getKey() == key }
    }

    override fun getComponentInfo(uniqueId: UUID): NetworkComponentInfo? {
        if(networkComponents.parallelStream().filter { it.identifier == uniqueId }.count() > 1) return null
        return networkComponents.first { it.identifier == uniqueId }
    }

    override fun getComponentInfoAsync(key: String): FutureAction<NetworkComponentInfo> {
        val futureAction = FutureAction<NetworkComponentInfo>()
        val componentInfo = getComponentInfo(key)
        if(componentInfo != null) futureAction.complete(componentInfo)
        else futureAction.completeExceptionally(Exception("No component with key $key found"))
        return futureAction
    }

    override fun getComponentInfoAsync(uniqueId: UUID): FutureAction<NetworkComponentInfo> {
        val futureAction = FutureAction<NetworkComponentInfo>()
        val componentInfo = getComponentInfo(uniqueId)
        if(componentInfo != null) futureAction.complete(componentInfo)
        else futureAction.completeExceptionally(Exception("No component with uniqueId $uniqueId found"))
        return futureAction
    }

    override fun getComponentInfos(type: NetworkComponentType): List<NetworkComponentInfo> {
        return networkComponents.filter { it.type == type }
    }

    override fun getComponentInfos(): List<NetworkComponentInfo> {
        return networkComponents
    }

    override fun getComponentInfosAsync(type: NetworkComponentType): FutureAction<List<NetworkComponentInfo>> {
        val futureAction = FutureAction<List<NetworkComponentInfo>>()
        futureAction.complete(getComponentInfos(type))
        return futureAction
    }

    override fun getComponentInfosAsync(): FutureAction<List<NetworkComponentInfo>> {
        val futureAction = FutureAction<List<NetworkComponentInfo>>()
        futureAction.complete(getComponentInfos())
        return futureAction
    }


}