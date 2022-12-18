package net.dustrean.api.paper.event

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.paper.CorePaperAPI.getPlayerManager
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(null)
        val deferredPlayer = ICoreAPI.INSTANCE.getPlayerManager().getPlayerByUUID(event.player.uniqueId)
        deferredPlayer.invokeOnCompletion {
            if (it != null) event.player.kick(Component.text("§4An error occurred while loading your data. Please try again later or create a ticket."))
            val player = deferredPlayer.getCompleted()
                ?: let {
                    event.player.kick(Component.text("§4An error occurred while loading your data. Please try again later or create a ticket."))
                    return@invokeOnCompletion
                }
            player.lastServer = ICoreAPI.INSTANCE.getNetworkComponentInfo()
            player.update()
        }
    }
}