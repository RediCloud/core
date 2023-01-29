package net.dustrean.api.packet.connect

import kotlinx.coroutines.launch
import net.dustrean.api.ICoreAPI
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.Packet
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