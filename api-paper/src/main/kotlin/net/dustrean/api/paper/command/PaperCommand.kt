package net.dustrean.api.paper.command

import net.dustrean.api.command.ICommand
import net.dustrean.api.command.data.CommandData
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.command.Command as BukkitCommand

open class PaperCommand(
    override val commandName: String,
    override val commandAliases: Array<String>,
    override val commandDescription: String,
    override val commandPermission: String
) : BukkitCommand(commandName, commandDescription, commandPermission, commandAliases.toMutableList()), ICommand {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        }
        PaperCommandManager.handleCommand(PaperCommandPlayer(sender), this, args.toList())
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        if (sender !is Player) {
            return mutableListOf()
        }

        return PaperCommandManager.handleTabComplete(PaperCommandPlayer(sender), this, args.joinToString(" "))
            .toMutableList()
    }

    override val commands = arrayListOf<CommandData>()

}