package dev.redicloud.api.velocity.command

import com.velocitypowered.api.command.Command
import com.velocitypowered.api.proxy.Player
import eu.cloudnetservice.modules.bridge.player.CloudPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import dev.redicloud.api.command.CommandManager
import dev.redicloud.api.command.ICommand
import dev.redicloud.api.command.ICommandActor
import dev.redicloud.api.velocity.VelocityCoreAPI

@OptIn(DelicateCoroutinesApi::class)
object VelocityCommandManager : CommandManager() {

    override fun registerCommand(command: ICommand) {

        if (command !is Command) {
            return
        }

        VelocityCoreAPI.proxyServer.commandManager.register(
            DefaultCommandMeta(command.commandAliases),
            command
        )

        loadSubCommands(command)
    }

    override fun getPlayer(clazz: Class<*>, player: ICommandActor): Any? {

        return when (clazz.typeName) {
            Player::class.java.typeName -> VelocityCoreAPI.proxyServer.getPlayer(player.uuid).get()
            CloudPlayer::class.java.typeName -> TODO()
            // add more type to parse/support here
            else -> null
        }

    }
}