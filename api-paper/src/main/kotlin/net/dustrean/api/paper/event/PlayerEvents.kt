package net.dustrean.api.paper.event

import kotlinx.coroutines.runBlocking
import net.dustrean.api.ICoreAPI
import net.dustrean.api.utils.fetcher.UniqueIdFetcher
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = runBlocking {
        event.joinMessage(null)
        val player = ICoreAPI.INSTANCE.playerManager.getPlayerByUUID(event.player.uniqueId)
        if (player == null) {
            event.player.kick(Component.text("§4An error occurred while loading your data. Please try again later or create a ticket."))
            return@runBlocking
        }

        player.lastServer = ICoreAPI.INSTANCE.networkComponentInfo
        player.update()

        UniqueIdFetcher.registerCache(event.player.uniqueId, player.name)
    }
}