package com.dustrean.api.packet.response

import com.dustrean.api.packet.Packet
import java.lang.Exception

//TODO make response packet easier to send

abstract class ResponseablePacket : Packet() {
    fun createErrorResponsePacket(exception: Exception): ErrorResponsePacket{
        val response = ErrorResponsePacket()
        response.packetData.responsePacketData = packetData
        response.setException(exception)
        return response
    }
}