package net.dustrean.api.command

interface ICommandManager {

    fun handleCommand(player: CommandActor, command: ICommand, args: List<String>)

    fun registerCommand(command: ICommand)
}