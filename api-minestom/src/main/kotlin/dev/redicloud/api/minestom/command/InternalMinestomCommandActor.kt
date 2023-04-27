package dev.redicloud.clients.minestom.command

import dev.redicloud.api.command.ICommandActor
import net.minestom.server.entity.Player

class InternalMinestomCommandActor(private val player: Player) : ICommandActor {

    override val uuid = player.uuid

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(message)


}