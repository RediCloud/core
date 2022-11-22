package com.dustrean.api.packet

import com.dustrean.api.network.INetworkComponentInfo

interface IPacketManager {
    fun registerPacket(packet: IPacket)

    fun unregisterPacket(packet: IPacket)

    fun isRegistered(packet: IPacket): Boolean

    fun sendPacket(packet: IPacket, receivers: Array<INetworkComponentInfo>)
}