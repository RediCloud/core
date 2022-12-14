package net.dustrean.api.player

import kotlinx.coroutines.Deferred
import java.util.*

interface IPlayerManager {

    fun getPlayerByName(name: String): Deferred<IPlayer?>

    fun getPlayerByUUID(uuid: UUID): Deferred<IPlayer?>

    fun getOnlinePlayers()
}