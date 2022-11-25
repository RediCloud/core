package net.dustrean.api.packet

import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.redis.RedisConnection
import org.redisson.api.RTopic
import java.util.*

class PacketManager(
    val networkComponentInfo: NetworkComponentInfo,
    val connection: RedisConnection,
    val topic: RTopic = connection.redisClient.getTopic("packet")
) : IPacketManager {

    companion object {
        lateinit var INSTANCE: PacketManager
    }

    init {
        INSTANCE = this
    }

    private val packets = arrayListOf<Class<out Packet>>()
    val waitingForResponse = hashMapOf<UUID, PacketData>()
    private var receiver: PacketReceiver = PacketReceiver(this)

    override fun <T : Packet> isRegistered(packet: T): Boolean = packets.contains(packet::class.java)

    override fun <T : Packet> registerPacket(packet: T) {
        packets.add(packet::class.java)
        receiver.connectPacketListener(packet::class.java)
    }

    override fun <T : Packet> unregisterPacket(packet: T) {
        packets.remove(packet::class.java)
        receiver.disconnectPacketListener(packet::class.java)
    }

    override fun <T : Packet> sendPacket(packet: T) {
        if (!isRegistered(packet)) throw Exception("Packet is not registered")
        packet.packetData.senderComponent = networkComponentInfo
        topic.publish(packet)
    }

    override fun <T : Packet> sendPacketAsync(packet: T) {
        if (!isRegistered(packet)) throw Exception("Packet is not registered")
        packet.packetData.senderComponent = networkComponentInfo
        topic.publishAsync(packet)
    }
}