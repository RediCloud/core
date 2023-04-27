package dev.redicloud.clients.paper.command

import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.command.ICommand
import dev.redicloud.api.command.data.CommandData
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.command.Command as BukkitCommand

open class InternalPaperCommand(
    override val commandName: String,
    override val commandAliases: Array<String>,
    override val commandDescription: String,
    override val commandPermission: String
) : BukkitCommand(commandName, commandDescription, commandPermission, commandAliases.toMutableList()), ICommand {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to execute this command!")
            return false
        }
        ICoreAPI.INSTANCE.commandManager.handleCommand(InternalPaperCommandActor(sender), this, args.toList())
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        if (sender !is Player) {
            return mutableListOf()
        }

        return  ICoreAPI.INSTANCE.commandManager.handleTabComplete(InternalPaperCommandActor(sender), this, args.joinToString(" "))
            .toMutableList()
    }

    override fun loadedSubCommands() {}

    override val commands = arrayListOf<CommandData>()

}