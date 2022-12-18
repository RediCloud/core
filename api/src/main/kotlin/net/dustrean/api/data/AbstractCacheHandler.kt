package net.dustrean.api.data

import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

abstract class AbstractCacheHandler<T : AbstractDataObject> {

    val currentCached = mutableSetOf<NetworkComponentInfo>()

    fun getCurrentNetworkComponents(): Set<NetworkComponentInfo> {
        return Collections.unmodifiableSet(currentCached)
    }

    abstract fun getCacheNetworkComponents(): FutureAction<Set<NetworkComponentInfo>>

}