package dev.redicloud.api.data

import dev.redicloud.api.network.NetworkComponentInfo

abstract class AbstractCacheHandler {

    val currentCached = mutableSetOf<NetworkComponentInfo>()

    fun getCurrentNetworkComponents(): Set<NetworkComponentInfo> = currentCached.toSet()

    abstract suspend fun getCacheNetworkComponents(): Set<NetworkComponentInfo>

}