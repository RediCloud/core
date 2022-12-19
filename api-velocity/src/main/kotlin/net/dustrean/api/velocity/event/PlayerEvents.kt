package net.dustrean.api.velocity.event

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.runBlocking
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.player.Player
import net.dustrean.api.player.PlayerManager

class PlayerEvents(private val playerManager: PlayerManager) {
    @Subscribe
    fun onPlayerJoin(event: LoginEvent) = runBlocking j@{
        val player = runBlocking {
            playerManager.getPlayerByUUID(
                event.player.uniqueId
            )
        }
        playerManager.onlineFetcher.add(event.player.uniqueId)
        if (player != null) {
            if (player.name != event.player.username) {
                playerManager.nameFetcher.remove(player.name.lowercase())
                playerManager.nameFetcher[event.player.username.lowercase()] = event.player.uniqueId
                player.nameHistory.add(System.currentTimeMillis() to event.player.username)
                player.name = event.player.username
                // TODO: PlayerNameUpdate Event CoreAPI
            }

            if (player.getLastIp() != event.player.remoteAddress.address.hostAddress)
                player.ipHistory.add(System.currentTimeMillis() to event.player.remoteAddress.address.hostAddress)

            player.lastProxy = ICoreAPI.getInstance<CoreAPI>().getNetworkComponentInfo()
            player.currentlyOnline = true
            player.update()
            return@j
        }
        ICoreAPI.getInstance<CoreAPI>().getPlayerManager().createObject(Player(
            event.player.uniqueId, event.player.username, currentlyOnline = true
        ).apply {
            playerManager.nameFetcher[event.player.username.lowercase()] = event.player.uniqueId
            nameHistory.add(System.currentTimeMillis() to event.player.username)
            ipHistory.add(System.currentTimeMillis() to event.player.remoteAddress.address.hostAddress)
            lastProxy = ICoreAPI.INSTANCE.getNetworkComponentInfo()
        })
    }

    @Subscribe
    fun onPlayerDisconnect(event: DisconnectEvent) {
        playerManager.onlineFetcher.remove(event.player.uniqueId)
    }
}