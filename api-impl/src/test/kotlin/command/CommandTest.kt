package command

import java.util.*


fun main() {
    val command = CoinsCommand
    TestCommandManager.registerCommand(command)
    val player = TestCommandPlayer(UUID.randomUUID())

    command.execute(player, arrayOf())
    command.execute(player, arrayOf("100"))
    command.execute(player, arrayOf(UUID.randomUUID().toString(), "100"))

    command.suggest(player, arrayOf()).forEach { println(it) }
}

