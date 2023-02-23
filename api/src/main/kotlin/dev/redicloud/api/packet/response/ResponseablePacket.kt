package dev.redicloud.api.packet.response

import dev.redicloud.api.packet.Packet

//TODO make response packet easier to send

abstract class ResponseablePacket : Packet() {
    fun createErrorResponsePacket(exception: Exception): ErrorResponsePacket {
        val response = ErrorResponsePacket()
        response.packetData.responsePacketData = packetData
        response.setException(exception)
        return response
    }
}