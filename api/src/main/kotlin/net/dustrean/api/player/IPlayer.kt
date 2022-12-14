package net.dustrean.api.player

import kotlinx.coroutines.*
import net.dustrean.api.network.NetworkComponentInfo
import java.util.*

interface IPlayer {

    val uuid: UUID

    var name: String

    var languageID: Int

    var coins: Long

    fun getProxy(): Deferred<NetworkComponentInfo>

    fun getServer(): Deferred<NetworkComponentInfo>
}