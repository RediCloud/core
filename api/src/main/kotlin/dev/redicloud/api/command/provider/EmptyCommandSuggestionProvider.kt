package dev.redicloud.api.command.provider

import dev.redicloud.api.command.ICommandActor

class EmptyCommandSuggestionProvider : CommandSuggestionProvider {

    override fun getSuggestions(sender: ICommandActor, fullCommand: String, lastArgument: String): List<String> {
        return emptyList()
    }
}