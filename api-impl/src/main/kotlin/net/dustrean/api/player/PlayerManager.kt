package net.dustrean.api.player

import kotlinx.coroutines.*
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

class PlayerManager(core: ICoreAPI) : IPlayerManager, AbstractDataManager<Player>(
    "player",
    core.getRedisConnection(),
    Player::class.java
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getPlayerByUUID(uuid: UUID): Deferred<Player?> {
        return scope.async {
            withContext(Dispatchers.IO) {
                getObject(
                    uuid
                ).get()
            }
        }
    }

    override fun getPlayerByName(name: String): Deferred<Player?> {
        TODO()
    }

    override fun getOnlinePlayers() {
        TODO("Not yet implemented")
    }

    internal fun updatePlayer(player: Player): FutureAction<Player> { // TODO: POST DEFERRED
        return updateObject(player)
    }
}