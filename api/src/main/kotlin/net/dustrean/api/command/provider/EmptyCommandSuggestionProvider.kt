package net.dustrean.api.command.provider

import net.dustrean.api.command.ICommandPlayer

class EmptyCommandSuggestionProvider : CommandSuggestionProvider {

    override fun getSuggestions(sender: ICommandPlayer, fullCommand: String, lastArgument: String): List<String> {
        return emptyList()
    }
}