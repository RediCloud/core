package dev.redicloud.api.language.component.tablist

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class TabListComponentProvider : LanguageComponentProvider(LanguageType.TAB_LIST) {
    var header: Component = Component.empty()
    var footer: Component = Component.empty()
}