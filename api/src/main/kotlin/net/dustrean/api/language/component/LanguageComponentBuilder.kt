package net.dustrean.api.language.component

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.placeholder.PlaceholderProvider

abstract class LanguageComponentBuilder<T>(type: LanguageType) {

    var key: String = ""
    private val placeholderProvider: PlaceholderProvider<T> = PlaceholderProvider(this)

    fun placeholderProvider() = placeholderProvider

    abstract fun <T: LanguageComponentProvider> build(): T

    abstract class LanguageComponentProvider(val key: String, val type: LanguageType, val placeholderProvider: PlaceholderProvider<*>)

}