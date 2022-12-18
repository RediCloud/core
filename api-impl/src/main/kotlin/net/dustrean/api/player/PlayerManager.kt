package net.dustrean.api.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractDataManager
import java.util.*

class PlayerManager(core: ICoreAPI) : IPlayerManager, AbstractDataManager<Player>(
    "player",
    core.getRedisConnection(),
    Player::class.java
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getPlayerByUUID(uuid: UUID): IPlayer? {
        return withContext(Dispatchers.IO) {
            getObject(
                uuid
            )
        }

    }

    override suspend fun getPlayerByName(name: String): IPlayer? {
        TODO()
    }

    override suspend fun getOnlinePlayers(): Collection<IPlayer> {
        TODO("Not yet implemented")
    }

    internal suspend fun updatePlayer(player: Player): Player {
        return updateObject(player)
    }
}