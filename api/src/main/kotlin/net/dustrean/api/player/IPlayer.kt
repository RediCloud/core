package net.dustrean.api.player

import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.network.NetworkComponentInfo
import java.util.*

interface IPlayer : ILanguagePlayer {
    suspend fun update(): IPlayer

    override val uuid: UUID

    var name: String

    var coins: Long

    var connected: Boolean

    var lastServer: NetworkComponentInfo

    var lastProxy: NetworkComponentInfo

    val authentication: IPlayerAuthentication

    val nameHistory: MutableList<Pair<Long, String>>

    val sessions: MutableList<out IPlayerSession>


    fun isOnCurrent(): Boolean

    fun getCurrentSession(): IPlayerSession?

    fun getLastSession(): IPlayerSession?

    suspend fun connect(service: NetworkComponentInfo)

}