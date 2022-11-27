package net.dustrean.api.network

import net.dustrean.api.packet.futures.FutureAction
import java.util.*

interface INetworkComponentManager {
    fun getComponentInfo(key: String): NetworkComponentInfo?
    fun getComponentInfoAsync(key: String): FutureAction<NetworkComponentInfo>

    fun getComponentInfo(uniqueId: UUID): NetworkComponentInfo?
    fun getComponentInfoAsync(uniqueId: UUID): FutureAction<NetworkComponentInfo>

    fun getComponentInfos(type: NetworkComponentType): List<NetworkComponentInfo>
    fun getComponentInfosAsync(type: NetworkComponentType): FutureAction<List<NetworkComponentInfo>>

    fun getComponentInfos(): List<NetworkComponentInfo>
    fun getComponentInfosAsync(): FutureAction<List<NetworkComponentInfo>>
}