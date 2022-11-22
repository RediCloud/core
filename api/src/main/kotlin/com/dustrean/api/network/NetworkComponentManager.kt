package com.dustrean.api.network

import java.util.*

interface NetworkComponentManager {
    fun getComponentInfo(key: String): INetworkComponentInfo

    fun getComponentInfo(uniqueId: UUID): INetworkComponentInfo

    fun getComponentInfos(type: NetworkComponentType): List<INetworkComponentInfo>

    fun getComponentInfos(): List<INetworkComponentInfo>
}