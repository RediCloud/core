package net.dustrean.api.velocity.event

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.runBlocking
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.player.Player
import net.dustrean.api.player.PlayerManager

class PlayerEvents(private val playerManager: PlayerManager) {
    @Subscribe
    fun onPlayerJoin(event: LoginEvent) {
        val player = runBlocking {
            playerManager.getPlayerByUUID(
                event.player.uniqueId
            ).await()
        }
        if (player != null) {
            if (
                player.name != event.player.username
            ) {
                player.nameHistory.add(System.currentTimeMillis() to event.player.username)
                player.name = event.player.username
                // TODO: PlayerNameUpdate Event CoreAPI
            }
            player.lastProxy = ICoreAPI.getInstance<CoreAPI>().getNetworkComponentInfo()
            player.currentlyOnline = true
            player.update().get()
            return
        }
        ICoreAPI.getInstance<CoreAPI>().getPlayerManager().createObject(
            Player(
                event.player.uniqueId,
                event.player.username,
                currentlyOnline = true
            ).apply {
                nameHistory.add(System.currentTimeMillis() to event.player.username)
                lastProxy = ICoreAPI.INSTANCE.getNetworkComponentInfo()
            }
        ).get()
    }
}