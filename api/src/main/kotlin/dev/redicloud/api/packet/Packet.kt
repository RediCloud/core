package dev.redicloud.api.packet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import dev.redicloud.api.ICoreAPI
import java.io.Serializable

abstract class Packet(
    var packetData: PacketData = PacketData()
) : Serializable {

    companion object {
        val scope = CoroutineScope(Dispatchers.IO)
    }

    abstract fun received()

    suspend fun sendPacket() {
        ICoreAPI.INSTANCE.packetManager.sendPacket(this)
    }

}