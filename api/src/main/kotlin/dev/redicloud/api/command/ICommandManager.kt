package dev.redicloud.api.command

import dev.redicloud.api.utils.parser.string.IStringTypeParser

interface ICommandManager {

    fun handleCommand(player: ICommandActor, command: ICommand, args: List<String>)

    fun handleTabComplete(player: ICommandActor, command: ICommand, message: String): List<String>

    fun registerCommand(command: ICommand)

    fun registerTypeParser(typeParser: IStringTypeParser<*>)

}