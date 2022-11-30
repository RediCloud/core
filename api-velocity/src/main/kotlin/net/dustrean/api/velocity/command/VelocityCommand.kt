package net.dustrean.api.velocity.command

import com.velocitypowered.api.command.RawCommand
import com.velocitypowered.api.proxy.Player
import net.dustrean.api.command.Command

open class VelocityCommand(
    name: String,
    aliases: Array<String> = arrayOf(),
    description: String = "",
    permission: String = ""
) : RawCommand, Command(name, aliases, description, permission) {

    override fun execute(invocation: RawCommand.Invocation?) {
        if (invocation?.source() !is Player) {
            return
        }
        VelocityCommandManager.handleCommand(
            VelocityCommandPlayer(invocation as Player),
            this,
            invocation.arguments().split(" ")
        )
    }

    override fun suggest(invocation: RawCommand.Invocation): List<String> {
        if (invocation.source() !is Player) {
            return emptyList()
        }
        return VelocityCommandManager.handleTabComplete(
            VelocityCommandPlayer(invocation as Player),
            this,
            invocation.arguments()
        )
    }

}