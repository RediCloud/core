package net.dustrean.api.command.provider

import net.dustrean.api.command.ICommandActor

class EmptyCommandSuggestionProvider : CommandSuggestionProvider {

    override fun getSuggestions(sender: ICommandActor, fullCommand: String, lastArgument: String): List<String> {
        return emptyList()
    }
}