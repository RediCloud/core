package net.dustrean.api.command

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dustrean.api.command.annotations.CommandArgument
import net.dustrean.api.command.annotations.CommandSubPath
import net.dustrean.api.command.data.CommandData
import net.dustrean.api.command.data.CommandParameterData
import net.dustrean.api.utils.parser.string.StringParser
import org.jetbrains.annotations.NotNull
import org.slf4j.LoggerFactory
import kotlin.reflect.jvm.kotlinFunction

@DelicateCoroutinesApi
abstract class CommandManager {

    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    fun handleCommand(player: ICommandPlayer, command: ICommand, args: List<String>) {

        val commandData = getMatchingCommandData(command, args) ?: return

        if (!player.hasPermission(commandData.permission)) {
            player.sendMessage("§cYou don't have permission to execute this command. (TODO Langsystem ${this.javaClass.name}:17) ")
            return
        }

        val list = arrayListOf<Any?>()

        val playerClass = commandData.method.parameters.first().type

        if (playerClass.isAssignableFrom(player.baseClass)) {
            list.add(player)
        } else {

            val playerObject = getPlayer(playerClass, player)

            if (playerObject != null) {
                list.add(playerObject)
            } else {
                logger.error("Player class ${playerClass.name} is not assignable from ${player.baseClass.name} and no player object was found.")
                return
            }
        }

        for (parameterData in commandData.parameterDataList) {

            val parameterName: String = parameterData.name
            val indexOfParameter = commandData.getIndexOfParameter(parameterName)
            val parameterValue = args.getOrNull(indexOfParameter) ?: if (!parameterData.nullable) return else null

            val obj = if (parameterValue != null) try {
                if (parameterData.type == String::class.java) {
                    parameterValue
                } else {
                    StringParser.parseToObject(parameterValue, parameterData.type)
                }
            } catch (e: Exception) {
                player.sendMessage("§cCan't parse parameter at index $indexOfParameter(\"$parameterValue\") to class ${parameterData.type.simpleName}")
                return
            } else null

            if (!parameterData.nullable) {

                if (parameterData.type.isEnum) {
                    val clazz = parameterData.type
                    val enumValues = clazz.enumConstants
                    player.sendMessage("Allowed are: " + enumValues.joinToString(", "))
                }
                return
            }
            list.add(obj)
        }

        try {
            commandData.method.invoke(commandData.source, *list.toTypedArray())
        } catch (e: Exception) {
            logger.error("Error while executing command", e)
        }

    }

    fun handleTabComplete(player: ICommandPlayer, command: ICommand, message: String): List<String> {

        val messageArray = message.split(" ").map { it.trim() }
        val suggestions = HashSet<String>()
        val dataList = getAvailableArgsMatchingCommandData(messageArray.dropLast(1).joinToString(" "), command)

        dataList.forEach {

            val path = it.path
            val pathArray = path.split(" ")

            if (pathArray.size == messageArray.lastIndex) {
                return@forEach
            }

            val currentPathValue = pathArray[messageArray.lastIndex]

            val permission = it.permission
            if (permission.isEmpty() || player.hasPermission(permission)) {
                if (isParameter(currentPathValue)) {
                    val commandParameterData = it.getParameterDataByNameWithBraces(currentPathValue) ?: return@forEach
                    suggestions.addAll(
                        commandParameterData.provider.getSuggestions(
                            player,
                            message,
                            messageArray.last()
                        )
                    )
                } else {
                    suggestions.add(currentPathValue)
                }
            }
        }

        return suggestions.filter { it.toLowerCase().startsWith(messageArray.last().toLowerCase()) }
    }

    fun loadSubCommands(command: ICommand) {
        val commandClass = command::class.java

        GlobalScope.launch {
            for (method in commandClass.declaredMethods) {
                launch {
                    val commandSubPath = method.getAnnotation(CommandSubPath::class.java) ?: return@launch
                    val commandArgs = ArrayList<CommandParameterData>()

                    for ((index, parameter) in method.parameters.withIndex()) {

                        if (index != 0) {

                            val commandArgument = parameter.getAnnotation(CommandArgument::class.java)

                            if (commandArgument != null) {
                                commandArgs.add(
                                    CommandParameterData(
                                        parameter.type,
                                        commandArgument.suggestionProvider.java.getConstructor().newInstance(),
                                        commandArgument.name,
                                        try {
                                            (method.kotlinFunction?.parameters?.get(index)?.type?.isMarkedNullable
                                                ?: false)
                                        } catch (_: Throwable) {
                                            false
                                        } || !parameter.isAnnotationPresent(NotNull::class.java)
                                    )
                                )
                            }
                        }
                    }

                    command.commands.add(
                        CommandData(
                            commandSubPath.path,
                            method,
                            command,
                            commandSubPath.permission,
                            commandArgs
                        )
                    )
                }
            }
        }
    }

    private fun getAvailableArgsMatchingCommandData(message: String, command: ICommand): List<CommandData> {
        val messageArray = message.split(" ")
        val dataList = getCommandDataByMinimumArgumentLength(messageArray.size, command)
        return dataList.filter { commandData ->
            commandData.getAllPathsWithAliases().any {
                val path = it.trim()
                val pathArray = path.split(" ")

                messageArray.withIndex().all { (index, value) ->
                    val pathValue = pathArray[index]
                    isParameter(pathValue) || value.equals(pathValue, ignoreCase = true)
                }
            }
        }
    }

    private fun getCommandDataByMinimumArgumentLength(length: Int, command: ICommand) =
        command.commands.filter { it.path.split(" ").size >= length }

    private fun getCommandDataByArgLength(command: ICommand, length: Int): List<CommandData> =
        command.commands.filter {
            if (it.path.isBlank()) {
                0
            } else {
                it.path.split(" ").size
            } == length
        }

    private fun getMatchingCommandData(command: ICommand, args: List<String>): CommandData? {

        val commandDataList = getCommandDataByArgLength(command, args.size)

        if (commandDataList.size == 1) {
            return commandDataList.first()
        }

        return commandDataList.firstOrNull { commandData ->

            println(commandData.method.name)

            val pathArray = commandData.path.split(" ")

            pathArray.withIndex()
                .all { isParameter(it.value) || it.value.equals(args[it.index], ignoreCase = true) }
        }
    }

    private fun isParameter(s: String) = s.startsWith("<") && s.endsWith(">")

    abstract fun registerCommand(command: ICommand)

    abstract fun getPlayer(clazz: Class<*>, player: ICommandPlayer): Any?
}