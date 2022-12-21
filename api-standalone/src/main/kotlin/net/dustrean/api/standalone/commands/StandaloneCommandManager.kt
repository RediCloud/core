package net.dustrean.api.standalone.commands

import kotlinx.coroutines.DelicateCoroutinesApi
import net.dustrean.api.command.CommandManager
import net.dustrean.api.command.ICommand
import net.dustrean.api.command.ICommandActor

//TODO add support for standalone commands
@OptIn(DelicateCoroutinesApi::class)
object StandaloneCommandManager : CommandManager() {

    override fun registerCommand(command: ICommand) {
        TODO("Not yet implemented")
    }

    override fun getPlayer(clazz: Class<*>, player: ICommandActor): Any? {
        TODO("Not yet implemented")
    }

}