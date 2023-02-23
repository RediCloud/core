package dev.redicloud.api.command.annotations

import dev.redicloud.api.command.provider.CommandSuggestionProvider
import dev.redicloud.api.command.provider.EmptyCommandSuggestionProvider
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandArgument(
    val name: String,
    val suggestionProvider: KClass<out CommandSuggestionProvider> = EmptyCommandSuggestionProvider::class
)