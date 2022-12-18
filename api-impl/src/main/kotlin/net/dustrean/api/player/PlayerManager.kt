package net.dustrean.api.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractDataManager
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RLocalCachedMap
import java.util.*

class PlayerManager(core: ICoreAPI) : IPlayerManager, AbstractDataManager<Player>(
    "player", core.getRedisConnection(), Player::class.java
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    val nameFetcher = core.getRedisConnection().getRedissonClient().getLocalCachedMap(
        "fetcher:player_name",
        LocalCachedMapOptions.defaults<String, UUID>().storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
    )
    val onlineFetcher = core.getRedisConnection().getRedissonClient().getList<UUID>("fetcher:player_online")

    override suspend fun getPlayerByUUID(uuid: UUID): IPlayer? {
        try {
            return getObject(uuid)
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    override suspend fun getPlayerByName(name: String): IPlayer? =
        nameFetcher.get(name)?.let { getPlayerByUUID(it) }

    override suspend fun getOnlinePlayers(): Collection<IPlayer> =
        onlineFetcher.map { getPlayerByUUID(it)!! }

    internal suspend fun updatePlayer(player: Player): Player =
        updateObject(player)

}