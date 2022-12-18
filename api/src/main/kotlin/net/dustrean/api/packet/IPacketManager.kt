package net.dustrean.api.packet

interface IPacketManager {
    fun<T: Packet> registerPacket(packet: T)

    fun<T: Packet> unregisterPacket(packet: T)

    fun<T: Packet> isRegistered(packet: T): Boolean

    suspend fun<T: Packet> sendPacket(packet: T)
}