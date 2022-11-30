package net.dustrean.api.command.provider

import net.dustrean.api.command.CommandActor

class EmptyCommandSuggestionProvider : CommandSuggestionProvider {

    override fun getSuggestions(sender: CommandActor, fullCommand: String, lastArgument: String): List<String> {
        return emptyList()
    }
}