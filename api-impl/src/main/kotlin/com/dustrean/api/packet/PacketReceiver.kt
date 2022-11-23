package com.dustrean.api.packet

import com.dustrean.api.packet.response.PacketResponse
import org.redisson.api.RFuture

class PacketReceiver(
    val packetManager: PacketManager
) {

    val listener: HashMap<Class<out Packet>, Int> = hashMapOf()

    private fun <T : Packet> receive(packet: T) {

        if (!packetManager.isRegistered(packet)) {
            throw IllegalArgumentException("Packet " + packet::class.java.name + " is not registered")
        }

        if (!packet.packetData.hasReceiver(packetManager.networkComponentInfo)) return
        if (packet.packetData.senderComponent == packetManager.networkComponentInfo && packet.packetData.allowSenderAsReceiver) return

        if (packet.packetData.responsePacketData != null) {
            if (!packetManager.waitingForResponse.containsKey(packet.packetData.responsePacketData!!.packetId)) {
                throw IllegalStateException(
                    "Packet " + packet::class.java.simpleName + "@" + packet.packetData.responsePacketData!!.packetId + " is not waiting for response"
                )
            }

            packet.received()

            val future =
                packetManager.waitingForResponse[packet.packetData.responsePacketData!!.packetId]!!.futureResponse
            if (!future!!.isFinishedAnyway()) future.complete(packet as PacketResponse)
            packetManager.waitingForResponse.remove(packet.packetData.responsePacketData!!.packetId)

            return
        }

        packet.received()
    }

    fun <T : Packet> connectPacketListener(packetClass: Class<T>) {
        val future: RFuture<Int> = packetManager.topic.addListenerAsync(packetClass, { _, packet -> receive(packet) })

        future.whenComplete(({ result, throwable ->
            run {
                if (throwable != null) {
                    throwable.printStackTrace()
                    return@run
                }
                listener.put(packetClass, result)
            }
        }))
    }

    fun <T : Packet> disconnectPacketListener(packetClass: Class<T>) {
        if (!isPacketListenerConnected(packetClass)) return
        val id = listener[packetClass]!!
        packetManager.topic.removeListenerAsync(id)
        listener.remove(packetClass)
    }

    fun <T : Packet> isPacketListenerConnected(packetClass: Class<T>): Boolean = listener.containsKey(packetClass)
}