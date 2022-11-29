package net.dustrean.api.paper.command

import net.dustrean.api.command.ICommandPlayer
import org.bukkit.entity.Player

class PaperCommandPlayer(val player: Player) : ICommandPlayer {

    override val uuid = player.uniqueId

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(message)
}