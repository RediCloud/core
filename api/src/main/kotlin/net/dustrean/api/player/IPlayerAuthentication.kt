package net.dustrean.api.player

interface IPlayerAuthentication {

    var passwordHash: String
    var passwordLogRounds: Int
    var ip: String
    var lastVerify: Long
    var cracked: Boolean
    val loginProcess: Boolean

    fun isLoggedIn(player: IPlayer): Boolean

}