@file:OptIn(DelicateCoroutinesApi::class)

package net.dustrean.api.minestom.command

import eu.cloudnetservice.modules.bridge.player.CloudPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import net.dustrean.api.command.CommandManager
import net.dustrean.api.command.ICommand
import net.dustrean.api.command.ICommandActor
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

object MinestomCommandManager : CommandManager() {

    override fun registerCommand(command: ICommand) {

        if (command !is Command) {
            return
        }

        MinecraftServer.getCommandManager().register(command)

        loadSubCommands(command)
    }

    override fun getPlayer(clazz: Class<*>, player: ICommandActor): Any? {

        return when (clazz.typeName) {
            Player::class.java.typeName -> MinecraftServer.getConnectionManager().getPlayer(player.uuid)
            CloudPlayer::class.java.typeName -> TODO()
            // add more type to parse/support here
            else -> null
        }
    }
}