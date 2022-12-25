package net.dustrean.api.player

import org.redisson.api.RList
import org.redisson.api.RLocalCachedMap
import java.util.*

interface IPlayerManager {

    val nameFetcher: RLocalCachedMap<String, UUID>
    val onlineFetcher: RList<UUID>

    suspend fun getPlayerByName(name: String): IPlayer?
    suspend fun getPlayerByUUID(uuid: UUID): IPlayer?
    suspend fun getOnlinePlayers(): Collection<IPlayer>
}