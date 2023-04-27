package dev.redicloud.api.command

import dev.redicloud.api.command.data.CommandData

interface ICommand {

    val commands: ArrayList<CommandData>

    val commandName: String
    val commandAliases: Array<String>
    val commandDescription: String
    val commandPermission: String

    fun loadedSubCommands()

}