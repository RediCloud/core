package net.dustrean.api.language.component.tablist

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentBuilder
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.text.Component

class TabListComponentBuilder : LanguageComponentBuilder<TabListComponentBuilder>(LanguageType.TAB_LIST) {

    private var header: Component = Component.text("!!")
    private var footer: Component = Component.text("!!")

    fun header(component: Component) = apply { this.header = component }
    fun footer(component: Component) = apply { this.footer = component }

    override fun <T : LanguageComponentProvider> build(): T {
        if (key().isEmpty()) throw IllegalArgumentException("Key cannot be empty")
        if (header == Component.text("!!")) throw IllegalArgumentException("Component is not set")
        if (footer == Component.text("!!")) throw IllegalArgumentException("Component is not set")
        return LanguageTabListComponentProvider(key(), placeholderProvider(), header, footer) as T
    }

    class LanguageTabListComponentProvider(
        key: String, placeholderProvider: PlaceholderProvider<TabListComponentBuilder>, val header: Component, val footer: Component
    ) : LanguageComponentProvider(key, LanguageType.TAB_LIST, placeholderProvider)

}