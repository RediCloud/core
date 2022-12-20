package net.dustrean.api.player

import net.dustrean.api.network.NetworkComponentInfo

interface IPlayerSession {

    var start: Long
    var end: Long
    var ip: String
    var proxyId: NetworkComponentInfo
    var premium: Boolean
    var authenticated: Boolean

    fun getDuration(): Long
    fun isActive(): Boolean

}