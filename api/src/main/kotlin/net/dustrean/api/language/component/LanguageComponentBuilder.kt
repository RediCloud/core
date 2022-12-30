package net.dustrean.api.language.component

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.placeholder.PlaceholderProvider

abstract class LanguageComponentBuilder<T>(type: LanguageType) {

    private var key: String = ""
    private val placeholderProvider: PlaceholderProvider<T> = PlaceholderProvider(this as T)

    fun key(key: String): T {
        this.key = key
        return this as T
    }
    fun key() = key

    fun placeholderProvider() = placeholderProvider

    abstract fun <T: LanguageComponentProvider> build(): T

    abstract class LanguageComponentProvider(val key: String, val type: LanguageType, val placeholderProvider: PlaceholderProvider)

}