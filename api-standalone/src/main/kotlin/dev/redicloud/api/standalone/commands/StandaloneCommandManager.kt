package dev.redicloud.api.standalone.commands

import kotlinx.coroutines.DelicateCoroutinesApi
import dev.redicloud.api.command.CommandManager
import dev.redicloud.api.command.ICommand
import dev.redicloud.api.command.ICommandActor

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