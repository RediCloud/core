package net.dustrean.api.velocity.command

import com.velocitypowered.api.proxy.Player
import net.dustrean.api.command.CommandActor
import net.kyori.adventure.text.Component
import java.util.*

class VelocityCommandActor(val player: Player) : CommandActor {

    override val uuid: UUID = player.uniqueId

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(Component.text(message))
}