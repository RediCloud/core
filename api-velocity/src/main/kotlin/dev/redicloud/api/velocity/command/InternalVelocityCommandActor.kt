package dev.redicloud.clients.velocity.command

import com.velocitypowered.api.proxy.Player
import dev.redicloud.api.command.ICommandActor
import net.kyori.adventure.text.Component
import java.util.*

class InternalVelocityCommandActor(private val player: Player) : ICommandActor {

    override val uuid: UUID = player.uniqueId

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(Component.text(message))
}