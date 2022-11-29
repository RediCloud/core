package net.dustrean.api.velocity.command

import com.velocitypowered.api.proxy.Player
import net.dustrean.api.command.ICommandPlayer
import net.kyori.adventure.text.Component
import java.util.*

class VelocityCommandPlayer(val player: Player) : ICommandPlayer {

    override val uuid: UUID = player.uniqueId

    override fun hasPermission(permission: String) = player.hasPermission(permission)

    override fun sendMessage(message: String) = player.sendMessage(Component.text(message))
}