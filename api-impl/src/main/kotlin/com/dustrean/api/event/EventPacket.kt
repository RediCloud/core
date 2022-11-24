package com.dustrean.api.event

import com.dustrean.api.packet.Packet

class EventPacket : Packet() {

    lateinit var event: CoreEvent

    override fun received() {
        EventManager.INSTANCE.callEvent0(event, true)
    }


}