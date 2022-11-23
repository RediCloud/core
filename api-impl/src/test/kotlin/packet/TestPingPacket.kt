package packet

import com.dustrean.api.packet.PacketManager
import com.dustrean.api.packet.response.ResponseablePacket

class TestPingPacket : ResponseablePacket() {

    var start: Long = -1

    override fun received() {
        val response = TestPingResponsePacket()
        response.packetData.receiverComponent.add(packetData.senderComponent)
        response.start = start
        PacketManager.instance.sendPacket(response)
    }

}