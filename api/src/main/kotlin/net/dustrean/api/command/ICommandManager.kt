package net.dustrean.api.command

interface ICommandManager {

    fun handleCommand(player: ICommandActor, command: ICommand, args: List<String>)

    fun registerCommand(command: ICommand)
}