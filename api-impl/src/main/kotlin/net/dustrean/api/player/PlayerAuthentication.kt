package net.dustrean.api.player

import com.google.gson.annotations.Expose
import net.dustrean.api.utils.crypt.UpdatableBCrypt
import kotlin.time.Duration.Companion.minutes

class PlayerAuthentication : IPlayerAuthentication {

    override var passwordHash: String = ""
    override var passwordLogRounds: Int = 10
    override var ip: String = Player.INVALID_IP
    override var lastVerify: Long = -1
    override var cracked: Boolean = true
    override var loginProcess: Boolean = false
    @Expose(deserialize = false, serialize = false)
    override var crypt: UpdatableBCrypt? = null

    override fun isLoggedIn(player: IPlayer): Boolean {
        if (!cracked) return true
        val session = player.getLastSession() ?: return false
        if (session.ip != ip) return false
        if (!player.connected) {
            if (System.currentTimeMillis() - session.end > 5.minutes.inWholeMilliseconds) return false
        }
        return true
    }

}