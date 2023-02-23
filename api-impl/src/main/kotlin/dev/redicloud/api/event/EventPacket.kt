package dev.redicloud.api.event

import dev.redicloud.api.packet.Packet

class EventPacket : Packet() {

    lateinit var event: CoreEvent

    override fun received() {
        EventManager.INSTANCE.callEvent0(event, true)
    }


}