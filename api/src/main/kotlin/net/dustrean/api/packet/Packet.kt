package net.dustrean.api.packet

import net.dustrean.api.ICoreAPI
import java.io.Serializable

abstract class Packet(
    var packetData: PacketData = PacketData()
) : Serializable {
    abstract fun received()

    suspend fun sendPacket(){
        ICoreAPI.INSTANCE.getPacketManager().sendPacket(this)
    }

}