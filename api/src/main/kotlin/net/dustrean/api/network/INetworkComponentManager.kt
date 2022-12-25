package net.dustrean.api.network

import org.redisson.api.RMap
import java.util.*

interface INetworkComponentManager {

    val networkComponents: RMap<String, NetworkComponentInfo>

    suspend fun getComponentInfo(key: String): NetworkComponentInfo?

    suspend fun getComponentInfo(identifier: UUID): NetworkComponentInfo?


    suspend fun getComponentInfos(type: NetworkComponentType): List<NetworkComponentInfo>

    suspend fun getComponentInfos(): List<NetworkComponentInfo>

}