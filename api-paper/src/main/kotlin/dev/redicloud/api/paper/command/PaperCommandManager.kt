package dev.redicloud.api.paper.command

import eu.cloudnetservice.modules.bridge.player.CloudPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import dev.redicloud.api.command.CommandManager
import dev.redicloud.api.command.ICommand
import dev.redicloud.api.command.ICommandActor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.entity.Player
import java.lang.reflect.Field

@OptIn(DelicateCoroutinesApi::class)
object PaperCommandManager : CommandManager() {

    override fun registerCommand(command: ICommand) {

        if (command !is Command) {
            return
        }

        val commandField: Field = Bukkit.getServer()::class.java.getDeclaredField("commandMap")
        commandField.isAccessible = true
        val commandMap: CommandMap = commandField.get(Bukkit.getServer()) as CommandMap
        commandMap.register(command.commandName, command)

        loadSubCommands(command)
    }

    override fun getPlayer(clazz: Class<*>, player: ICommandActor): Any? {

        return when (clazz.typeName) {
            Player::class.java.typeName -> Bukkit.getPlayer(player.uuid)
            CloudPlayer::class.java.typeName -> TODO()
            // add more type to parse/support here
            else -> null
        }
    }
}