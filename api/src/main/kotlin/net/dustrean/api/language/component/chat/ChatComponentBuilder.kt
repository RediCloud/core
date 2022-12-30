package net.dustrean.api.language.component.chat

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentBuilder
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.text.Component

class ChatComponentBuilder : LanguageComponentBuilder(LanguageType.CHAT_MESSAGE) {

    private var component: Component = Component.text("!!")

    fun component(component: Component) = apply { this.component = component }

    override fun <T : LanguageComponentProvider> build(): T {
        if (key().isEmpty()) throw IllegalArgumentException("Key cannot be empty")
        if (component == Component.text("!!")) throw IllegalArgumentException("Component is not set")
        return LanguageChatComponentProvider(key(), placeholderProvider(), component) as T
    }

    class LanguageChatComponentProvider(
        key: String, placeholderProvider: PlaceholderProvider, val component: Component
    ) : LanguageComponentProvider(key, LanguageType.CHAT_MESSAGE, placeholderProvider)

}