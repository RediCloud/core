package net.dustrean.api.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.packet.PacketManager
import net.dustrean.api.packet.connect.PlayerChangeServicePacket
import org.redisson.api.LocalCachedMapOptions
import java.util.*

class PlayerManager(core: ICoreAPI) : IPlayerManager, AbstractDataManager<Player>(
    "player", core.getRedisConnection(), Player::class.java
) {

    init {
        PacketManager.INSTANCE.registerPacket(PlayerChangeServicePacket())
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    override val nameFetcher = core.getRedisConnection().getRedissonClient().getLocalCachedMap(
        "fetcher:player_name",
        LocalCachedMapOptions.defaults<String, UUID>().storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
    )
    override val onlineFetcher = core.getRedisConnection().getRedissonClient().getList<UUID>("fetcher:player_online")

    override suspend fun getPlayerByUUID(uuid: UUID): Player? = try {
        getObject(uuid)
    } catch (e: NoSuchElementException) {
        null
    }

    override suspend fun getPlayerByName(name: String): Player? = nameFetcher.get(name)?.let { getPlayerByUUID(it) }

    override suspend fun getOnlinePlayers(): Collection<Player> = onlineFetcher.map { getPlayerByUUID(it)!! }

    internal suspend fun updatePlayer(player: Player): Player = updateObject(player)

}