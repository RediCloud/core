package net.dustrean.api.player

import net.dustrean.api.network.NetworkComponentInfo
import java.util.*

interface IPlayer {
    suspend fun update(): IPlayer

    val uuid: UUID

    var name: String

    var languageID: Int

    var coins: Long

    var currentlyOnline: Boolean

    var lastServer: NetworkComponentInfo

    var lastProxy: NetworkComponentInfo

    val nameHistory: MutableList<Pair<Long, String>>
}