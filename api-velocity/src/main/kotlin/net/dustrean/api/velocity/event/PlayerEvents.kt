package net.dustrean.api.velocity.event

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.runBlocking
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.player.Player
import net.dustrean.api.player.PlayerManager
import net.dustrean.api.player.PlayerSession
import kotlin.time.Duration.Companion.seconds

class PlayerEvents(private val playerManager: PlayerManager) {
    @Subscribe
    fun onPlayerJoin(event: LoginEvent) = runBlocking j@{
        val player = playerManager.getPlayerByUUID(
            event.player.uniqueId
        )
        playerManager.onlineFetcher.add(event.player.uniqueId)


        val session = PlayerSession()
        session.ip = event.player.remoteAddress.address.hostAddress
        session.start = System.currentTimeMillis()
        session.authenticated = true
        session.premium = true
        session.proxyId = ICoreAPI.INSTANCE.getNetworkComponentInfo()

        if (player != null) {
            if (player.name != event.player.username) {
                playerManager.nameFetcher.remove(player.name.lowercase())
                playerManager.nameFetcher[event.player.username.lowercase()] = event.player.uniqueId
                player.nameHistory.add(System.currentTimeMillis() to event.player.username)
                player.name = event.player.username
                // TODO: PlayerNameUpdate Event CoreAPI
            }

            player.lastProxy = ICoreAPI.getInstance<CoreAPI>().getNetworkComponentInfo()
            player.connected = true
            player.sessions.add(System.currentTimeMillis() to session)
            player.update()
            return@j
        }
        playerManager.nameFetcher[event.player.username.lowercase()] = event.player.uniqueId
        ICoreAPI.getInstance<CoreAPI>().getPlayerManager().createObject(Player(
            event.player.uniqueId, event.player.username, connected = true
        ).apply {
            nameHistory.add(System.currentTimeMillis() to event.player.username)
            lastProxy = ICoreAPI.INSTANCE.getNetworkComponentInfo()
            sessions.add(System.currentTimeMillis() to session)
        })
    }

    @Subscribe
    fun onPlayerDisconnect(event: DisconnectEvent) = runBlocking {
        val player = runBlocking {
            playerManager.getPlayerByUUID(
                event.player.uniqueId
            )
        }
        if (player != null) {
            val session = player.getCurrentSession()
            if(session != null){
                session.end = System.currentTimeMillis()
                if(session.getDuration() < 5.seconds.inWholeMilliseconds)
                    player.sessions.removeIf { it == session }
            }
            player.update()
        }
        playerManager.onlineFetcher.remove(event.player.uniqueId)
    }
}