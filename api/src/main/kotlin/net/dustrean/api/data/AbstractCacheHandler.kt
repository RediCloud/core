package net.dustrean.api.data

import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

abstract class AbstractCacheHandler<T : IDataObject> {

    val currentCached = mutableListOf<NetworkComponentInfo>()

    fun getCurrentNetworkComponents(): List<NetworkComponentInfo> {
        return Collections.unmodifiableList(currentCached)
    }

    abstract fun getCacheNetworkComponents(): FutureAction<List<NetworkComponentInfo>>

}