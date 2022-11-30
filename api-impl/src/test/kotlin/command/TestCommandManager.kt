package command

import net.dustrean.api.command.CommandManager
import net.dustrean.api.command.ICommand
import net.dustrean.api.command.ICommandPlayer
import net.dustrean.api.command.annotations.CommandArgument
import net.dustrean.api.command.annotations.CommandSubPath
import net.dustrean.api.command.data.CommandData
import net.dustrean.api.command.data.CommandParameterData
import org.jetbrains.annotations.NotNull
import kotlin.reflect.jvm.kotlinFunction

object TestCommandManager : CommandManager() {

    override fun registerCommand(command: ICommand) {

        val commandClass = command::class.java

        for (method in commandClass.declaredMethods) {

            val commandSubPath = method.getAnnotation(CommandSubPath::class.java) ?: continue
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

    override fun getPlayer(clazz: Class<*>, player: ICommandPlayer): Class<*>? {
        return null
    }
}