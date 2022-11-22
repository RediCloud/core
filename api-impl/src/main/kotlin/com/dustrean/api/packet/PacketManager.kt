package com.dustrean.api.packet

import com.dustrean.api.network.INetworkComponentInfo
import com.dustrean.api.redis.RedisConnection
import org.redisson.api.RTopic
import java.util.UUID

class PacketManager(
    val networkComponentInfo: INetworkComponentInfo,
    val connection: RedisConnection,
    val topic: RTopic = connection.redisClient.getTopic("packet")
) : IPacketManager {

    val packets = arrayListOf<Class<out Packet>>()
    val waitingForResponse = hashMapOf<UUID, PacketData>()
    var receiver: PacketReceiver = PacketReceiver(this)

    override fun isRegistered(packet: Packet): Boolean = packets.contains(packet::class.java)

    override fun registerPacket(packet: Packet) {
        packets.add(packet::class.java)
        receiver.connectPacketListener(packet::class.java)
    }

    override fun unregisterPacket(packet: Packet) {
        packets.remove(packet::class.java)
        receiver.disconnectPacketListener(packet::class.java)
    }

    override fun sendPacket(packet: Packet) {
        if (!isRegistered(packet)) throw Exception("Packet is not registered")
        topic.publish(packet)
    }

    override fun sendPacketAsync(packet: Packet) {
        if (!isRegistered(packet)) throw Exception("Packet is not registered")
        topic.publishAsync(packet)
    }


}