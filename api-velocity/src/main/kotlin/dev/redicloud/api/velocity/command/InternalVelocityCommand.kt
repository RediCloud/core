package dev.redicloud.clients.velocity.command

import com.velocitypowered.api.command.RawCommand
import com.velocitypowered.api.proxy.Player
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.command.Command
import net.kyori.adventure.text.Component

open class InternalVelocityCommand(
    name: String,
    aliases: Array<String> = arrayOf(),
    description: String = "",
    permission: String = ""
) : RawCommand, Command(name, aliases, description, permission) {

    override fun execute(invocation: RawCommand.Invocation?) {
        if (invocation?.source() !is Player) {
            invocation?.source()?.sendMessage(Component.text("You must be a player to execute this command!"))
            return
        }
        ICoreAPI.INSTANCE.commandManager.handleCommand(
            InternalVelocityCommandActor(invocation as Player),
            this,
            invocation.arguments().split(" ")
        )
    }

    override fun suggest(invocation: RawCommand.Invocation): List<String> {
        if (invocation.source() !is Player) {
            return emptyList()
        }
        return ICoreAPI.INSTANCE.commandManager.handleTabComplete(
            InternalVelocityCommandActor(invocation as Player),
            this,
            invocation.arguments()
        )
    }

    override fun loadedSubCommands() {}

}