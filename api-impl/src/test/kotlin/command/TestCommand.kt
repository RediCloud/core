package command

import net.dustrean.api.command.Command
import net.dustrean.api.command.ICommandActor
import net.dustrean.api.command.annotations.CommandArgument
import net.dustrean.api.command.annotations.CommandSubPath
import java.util.*

open class TestCommand() : Command("test", arrayOf("t"), "test command", "test.command") {

    fun execute(sender: ICommandActor, args: Array<String>) {
        TestCommandManager.handleCommand(sender, this, args.toList())
    }

    fun suggest(sender: ICommandActor, args: Array<String>): List<String> =
        TestCommandManager.handleTabComplete(sender, this, args.joinToString())
}

object CoinsCommand : TestCommand() {

    @CommandSubPath
    fun handle(player: ICommandActor) {

        println("Command without arguments: ${player.uuid}")
    }

    @CommandSubPath("<amount>", "nsg.staff")
    fun get(player: ICommandActor, @CommandArgument("amount") test: Int) {

        println("Command with arguments: ${player.uuid} $test")
    }

    @CommandSubPath("<player> <amount>")
    fun set(
        player: ICommandActor,
        @CommandArgument("player", suggestionProvider = TestCommandSuggestionProvider::class) target: UUID,
        @CommandArgument("amount") amount: Int
    ) {

        println("Command with arguments: ${player.uuid} $target $amount")
    }
}