package com.dustrean.api.packet

import com.dustrean.api.network.INetworkComponentInfo
import com.dustrean.api.redis.RedisConnection
import org.redisson.api.RTopic

class PacketManager(
    val networkComponentInfo: INetworkComponentInfo,
    val connection: RedisConnection,
    val topic: RTopic = connection.redisClient.getTopic("packet")
) : IPacketManager {

    val packets = arrayListOf<Class<out Packet>>()

    override fun isRegistered(packet: Packet): Boolean = packets.contains(packet::class.java)

    override fun registerPacket(packet: Packet) {
        packets.add(packet::class.java)
        TODO("Connect to packet listener")
    }

    override fun sendPacket(packet: Packet, receivers: Array<INetworkComponentInfo>) {
        if (!isRegistered(packet)) throw Exception("Packet is not registered")
        packet.getPacketDescription()
        topic.publish(packet)
    }

    override fun unregisterPacket(packet: Packet) {
        TODO("Not yet implemented")
    }

}