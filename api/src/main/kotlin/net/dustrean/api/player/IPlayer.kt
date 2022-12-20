package net.dustrean.api.player

import net.dustrean.api.network.NetworkComponentInfo
import java.util.*

interface IPlayer {
    suspend fun update(): IPlayer

    val uuid: UUID

    var name: String

    var languageID: Int

    var coins: Long

    var connected: Boolean

    var lastServer: NetworkComponentInfo

    var lastProxy: NetworkComponentInfo

    val nameHistory: MutableList<Pair<Long, String>>

    val sessions: MutableList<Pair<Long, IPlayerSession>>

    fun isOnline(): Boolean

    fun getCurrentSession(): IPlayerSession?

    fun getLastSession(): IPlayerSession?

}