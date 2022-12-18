package net.dustrean.api.player

import kotlinx.coroutines.*
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

interface IPlayer {

    fun update(): FutureAction<out IPlayer>

    val uuid: UUID

    var name: String

    var languageID: Int

    var coins: Long

    var currentlyOnline: Boolean

    var lastServer: NetworkComponentInfo

    var lastProxy: NetworkComponentInfo

    val nameHistory: MutableList<Pair<Long, String>>
}