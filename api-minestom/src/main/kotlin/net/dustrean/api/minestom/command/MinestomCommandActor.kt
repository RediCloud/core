package net.dustrean.api.minestom.command

import net.dustrean.api.command.CommandActor
import net.minestom.server.entity.Player

class MinestomCommandActor(val player: Player) : CommandActor {

    override val uuid = player.uuid

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(message)


}