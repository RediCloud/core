package net.dustrean.api.language.component.tablist

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class TabListComponentProvider : LanguageComponentProvider(LanguageType.TAB_LIST) {
    var header: Component = Component.empty()
    var footer: Component = Component.empty()
}