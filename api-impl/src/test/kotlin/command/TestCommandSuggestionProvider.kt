package command

import net.dustrean.api.command.ICommandActor
import net.dustrean.api.command.provider.CommandSuggestionProvider
import java.util.*

class TestCommandSuggestionProvider : CommandSuggestionProvider {
    override fun getSuggestions(sender: ICommandActor, fullCommand: String, lastArgument: String): List<String> {
        return listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
    }
}