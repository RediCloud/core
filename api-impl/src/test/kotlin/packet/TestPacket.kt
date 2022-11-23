package packet

import com.dustrean.api.packet.Packet

class TestPacket : Packet() {

    lateinit var message: String

    override fun received() {
        println("Received test-packet from ${packetData.senderComponent.getKey()} with message: $message")
    }

}