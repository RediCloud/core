package net.dustrean.api.packet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.dustrean.api.ICoreAPI
import java.io.Serializable

abstract class Packet(
    var packetData: PacketData = PacketData()
) : Serializable {

    companion object {
        val scope = CoroutineScope(Dispatchers.IO)
    }
    abstract fun received()

    suspend fun sendPacket(){
        ICoreAPI.INSTANCE.getPacketManager().sendPacket(this)
    }

}