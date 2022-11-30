package command

import net.dustrean.api.command.ICommandPlayer
import net.dustrean.api.command.provider.CommandSuggestionProvider
import java.util.*

class TestCommandSuggestionProvider : CommandSuggestionProvider {
    override fun getSuggestions(sender: ICommandPlayer, fullCommand: String, lastArgument: String): List<String> {
        return listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
    }
}