package dev.redicloud.clients.minestom.command

import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.command.ICommand
import dev.redicloud.api.command.data.CommandData
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.command.builder.Command as MinestomCommand

open class InternalMinestomCommand(
    override val commandName: String,
    override val commandAliases: Array<String>,
    override val commandDescription: String,
    override val commandPermission: String
) : MinestomCommand(commandName, *commandAliases), ICommand {

    override fun loadedSubCommands() {
        commands.forEach { commandData ->
            val arguments = listOf<Argument<*>>()
            commandData.path.split(" ").forEach {
                if (it.startsWith("<") && it.endsWith(">")) {
                    val parameterData = commandData.getParameterDataByName(it)
                    if (parameterData != null) {
                        when (parameterData.type) {
                            Integer::class.java -> arguments.plus(ArgumentType.Integer(it.drop(1).dropLast(1)))
                            String::class.java -> arguments.plus(ArgumentType.String(it.drop(1).dropLast(1)))
                            Boolean::class.java -> arguments.plus(ArgumentType.Boolean(it.drop(1).dropLast(1)))
                            Float::class.java -> arguments.plus(ArgumentType.Float(it.drop(1).dropLast(1)))
                            Double::class.java -> arguments.plus(ArgumentType.Double(it.drop(1).dropLast(1)))
                            Long::class.java -> arguments.plus(ArgumentType.Long(it.drop(1).dropLast(1)))
                            else -> arguments.plus(ArgumentType.String(it.drop(1).dropLast(1)))
                        }
                        return@forEach
                    }
                }
                arguments.plus(ArgumentType.String(it.drop(1).dropLast(1)))
            }
            addSyntax(CommandExecutor { sender, context ->
                if (sender !is Player) {
                    sender.sendMessage("You must be a player to execute this command")
                    return@CommandExecutor
                }
                ICoreAPI.INSTANCE.commandManager.handleCommand(
                    InternalMinestomCommandActor(sender), this, context.input.split(" ").subList(
                        1, context.input.split(" ").size
                    )
                )
            }, *arguments.toTypedArray())
        }
    }

    override val commands = arrayListOf<CommandData>()
}