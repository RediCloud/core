package net.dustrean.api.command.data

import net.dustrean.api.command.provider.CommandSuggestionProvider

class CommandParameterData(
    val type: Class<*>,
    val provider: CommandSuggestionProvider,
    val name: String,
    val nullable: Boolean
) {
}