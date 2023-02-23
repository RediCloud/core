package dev.redicloud.api.command.data

import dev.redicloud.api.command.provider.CommandSuggestionProvider

class CommandParameterData(
    val type: Class<*>,
    val provider: CommandSuggestionProvider,
    val name: String,
    val nullable: Boolean
)