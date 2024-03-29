package dev.redicloud.api.player

import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.data.AbstractDataManager
import dev.redicloud.api.event.CoreListener
import dev.redicloud.api.event.impl.CoreInitializedEvent
import dev.redicloud.api.packet.connect.PlayerChangeServicePacket
import dev.redicloud.api.utils.fetcher.UniqueIdFetcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.redisson.api.LocalCachedMapOptions
import java.util.*

class PlayerManager(val core: ICoreAPI) : IPlayerManager, AbstractDataManager<Player>(
    "player", core.redisConnection, Player::class.java
) {

    init {
        runBlocking {
            core.eventManager.registerListener(this@PlayerManager)
        }
    }

    @CoreListener
    fun onCoreInitialized(event: CoreInitializedEvent) {
        core.packetManager.registerPacket(PlayerChangeServicePacket())
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    override val nameFetcher = core.redisConnection.getRedissonClient().getLocalCachedMap(
        "fetcher:player_name",
        LocalCachedMapOptions.defaults<String, UUID>().storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
    )
    override val onlineFetcher = core.redisConnection.getRedissonClient().getList<UUID>("fetcher:player_online")

    override suspend fun getPlayerByUUID(uuid: UUID): Player? {
        try {
            val player = getObject(uuid)
            UniqueIdFetcher.registerCache(uuid, player.name)
            return player
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    override suspend fun getPlayerByName(name: String): Player? = nameFetcher.get(name)?.let { getPlayerByUUID(it) }

    override suspend fun getOnlinePlayers(): Collection<Player> = onlineFetcher.map { getPlayerByUUID(it)!! }

    internal suspend fun updatePlayer(player: Player): Player = updateObject(player)

}