package net.dustrean.api.network

import java.util.*

interface NetworkComponentManager {
    fun getComponentInfo(key: String): NetworkComponentInfo

    fun getComponentInfo(uniqueId: UUID): NetworkComponentInfo

    fun getComponentInfos(type: NetworkComponentType): List<NetworkComponentInfo>

    fun getComponentInfos(): List<NetworkComponentInfo>
}