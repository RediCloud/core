package net.dustrean.api.player

import kotlinx.coroutines.Deferred
import java.util.*
import kotlin.collections.Collection

interface IPlayerManager {
    suspend fun getPlayerByName(name: String): IPlayer?
    suspend fun getPlayerByUUID(uuid: UUID): IPlayer?
    suspend fun getOnlinePlayers(): Collection<IPlayer>
}