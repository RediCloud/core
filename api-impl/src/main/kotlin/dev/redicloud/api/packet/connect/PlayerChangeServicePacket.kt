package dev.redicloud.api.packet.connect

import kotlinx.coroutines.launch
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.packet.Packet
import java.util.*

class PlayerChangeServicePacket : Packet() {

    lateinit var uniqueId: UUID
    lateinit var networkComponentInfo: NetworkComponentInfo

    override fun received() {
        scope.launch {
            val player = ICoreAPI.INSTANCE.playerManager.getPlayerByUUID(uniqueId)
            if (player == null) throw NullPointerException("Player with UUID $uniqueId not found!")
            player.connect(networkComponentInfo)
        }
    }

}