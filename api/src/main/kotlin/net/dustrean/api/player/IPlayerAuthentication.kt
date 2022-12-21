package net.dustrean.api.player

import com.google.gson.annotations.Expose
import net.dustrean.api.utils.crypt.UpdatableBCrypt

interface IPlayerAuthentication {

    var passwordHash: String
    var passwordLogRounds: Int
    var ip: String
    var lastVerify: Long
    var cracked: Boolean
    val loginProcess: Boolean
    var crypt: UpdatableBCrypt?
    fun isLoggedIn(player: IPlayer): Boolean

}