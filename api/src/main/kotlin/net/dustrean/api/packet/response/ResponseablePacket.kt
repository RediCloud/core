package net.dustrean.api.packet.response

import net.dustrean.api.packet.Packet

//TODO make response packet easier to send

abstract class ResponseablePacket : Packet() {
    fun createErrorResponsePacket(exception: Exception): ErrorResponsePacket {
        val response = ErrorResponsePacket()
        response.packetData.responsePacketData = packetData
        response.setException(exception)
        return response
    }
}