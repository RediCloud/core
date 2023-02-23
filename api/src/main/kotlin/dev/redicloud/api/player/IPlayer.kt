package dev.redicloud.api.player

import dev.redicloud.api.language.ILanguagePlayer
import dev.redicloud.api.network.NetworkComponentInfo
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