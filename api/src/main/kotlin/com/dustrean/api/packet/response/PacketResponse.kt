package com.dustrean.api.packet.response

import com.dustrean.api.packet.Packet

abstract class PacketResponse : Packet() {
    var exception: String? = null
    var errorMessage: String? = null

    fun setException(exception: Exception) {
        this.exception = exception::class.java.name
        this.errorMessage = exception.message
    }

    override fun received() {}
}