package net.dustrean.api.minestom.command

import net.dustrean.api.command.ICommandPlayer
import net.minestom.server.entity.Player

class MinestomCommandPlayer(val player: Player) : ICommandPlayer {

    override val uuid = player.uuid

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(message)


}