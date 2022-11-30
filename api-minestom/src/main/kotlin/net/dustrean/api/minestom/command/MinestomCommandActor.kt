package net.dustrean.api.minestom.command

import net.dustrean.api.command.ICommandActor
import net.minestom.server.entity.Player

class MinestomCommandActor(val player: Player) : ICommandActor {

    override val uuid = player.uuid

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(message)


}