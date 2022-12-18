package net.dustrean.api.data

import net.dustrean.api.network.NetworkComponentInfo

abstract class AbstractCacheHandler {

    val currentCached = mutableSetOf<NetworkComponentInfo>()

    fun getCurrentNetworkComponents(): Set<NetworkComponentInfo> = currentCached.toSet()

    abstract suspend fun getCacheNetworkComponents(): Set<NetworkComponentInfo>

}