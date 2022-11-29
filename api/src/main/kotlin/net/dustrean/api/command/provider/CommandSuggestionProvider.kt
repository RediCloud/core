package net.dustrean.api.command.provider

import net.dustrean.api.command.ICommandPlayer

interface CommandSuggestionProvider {

    /**
     * Returns the suggestions for an argument
     * @param sender the sender of the tab request
     * @param fullCommand the full command so far including the last argument
     * @param lastArgument the last argument of the [fullCommand]
     * @return a list with arguments to suggest
     */
    fun getSuggestions(sender: ICommandPlayer, fullCommand: String, lastArgument: String): List<String>
}