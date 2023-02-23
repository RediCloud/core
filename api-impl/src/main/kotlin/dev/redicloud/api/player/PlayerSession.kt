package dev.redicloud.api.player

import dev.redicloud.api.network.NetworkComponentInfo
import java.io.Serializable

class PlayerSession : IPlayerSession, Serializable {

    override var ip: String = Player.INVALID_IP
    override var start: Long = -1L
    override var end: Long = -1L
    override var premium: Boolean = false
    override var authenticated: Boolean = false
    override var proxyId: NetworkComponentInfo = Player.INVALID_SERVICE

    override fun getDuration(): Long =
        if (end == -1L) System.currentTimeMillis() - start
        else end - start

    override fun isActive(): Boolean =
        end == -1L && start != -1L

}