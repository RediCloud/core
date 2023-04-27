package dev.redicloud.clients.paper.command

import dev.redicloud.api.command.ICommandActor
import org.bukkit.entity.Player

class InternalPaperCommandActor(private val player: Player) : ICommandActor {

    override val uuid = player.uniqueId

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(message)
}