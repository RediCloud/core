package packet

import com.dustrean.api.packet.response.ResponseablePacket

class TestPingResponsePacket : ResponseablePacket() {

    var start: Long = -1

    override fun received() {
        println("Latency: " + (System.currentTimeMillis() - start) + "ms | " + packetData.senderComponent.getKey())
    }

}