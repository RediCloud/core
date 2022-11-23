package com.dustrean.api.packet

interface IPacketManager {
    fun<T: Packet> registerPacket(packet: T)

    fun<T: Packet> unregisterPacket(packet: T)

    fun<T: Packet> isRegistered(packet: T): Boolean

    fun<T: Packet> sendPacket(packet: T)

    fun<T: Packet> sendPacketAsync(packet: T)
}