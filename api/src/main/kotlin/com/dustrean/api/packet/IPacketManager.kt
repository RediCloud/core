package com.dustrean.api.packet

import com.dustrean.api.network.INetworkComponentInfo

interface IPacketManager {
    fun registerPacket(packet: Packet)

    fun unregisterPacket(packet: Packet)

    fun isRegistered(packet: Packet): Boolean

    fun sendPacket(packet: Packet)

    fun sendPacketAsync(packet: Packet)
}