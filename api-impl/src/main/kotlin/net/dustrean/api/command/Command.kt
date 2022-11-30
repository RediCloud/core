package net.dustrean.api.command

import net.dustrean.api.command.data.CommandData

abstract class Command(
    override val commandName: String,
    override val commandAliases: Array<String> = arrayOf(),
    override val commandDescription: String = "",
    override val commandPermission: String = ""
) : ICommand {

    override val commands = arrayListOf<CommandData>()
}