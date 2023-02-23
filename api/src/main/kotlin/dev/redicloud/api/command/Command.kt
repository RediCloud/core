package dev.redicloud.api.command

import dev.redicloud.api.command.data.CommandData

abstract class Command(
    override val commandName: String,
    override val commandAliases: Array<String> = arrayOf(),
    override val commandDescription: String = "",
    override val commandPermission: String = ""
) : ICommand {

    override val commands = arrayListOf<CommandData>()
}