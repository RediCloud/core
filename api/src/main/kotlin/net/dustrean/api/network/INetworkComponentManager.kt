package net.dustrean.api.network

import net.dustrean.api.tasks.futures.FutureAction
import org.redisson.api.RList
import org.redisson.api.RMap
import java.util.*

interface INetworkComponentManager {

    val networkComponents: RMap<String, NetworkComponentInfo>

    fun getComponentInfo(key: String): FutureAction<NetworkComponentInfo>

    fun getComponentInfo(uniqueId: UUID): FutureAction<NetworkComponentInfo>


    fun getComponentInfos(type: NetworkComponentType): FutureAction<List<NetworkComponentInfo>>

    fun getComponentInfos(): FutureAction<List<NetworkComponentInfo>>

}