package net.dustrean.api.command.annotations

import net.dustrean.api.command.provider.CommandSuggestionProvider
import net.dustrean.api.command.provider.EmptyCommandSuggestionProvider
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandArgument(
    val name: String,
    val suggestionProvider: KClass<out CommandSuggestionProvider> = EmptyCommandSuggestionProvider::class
)