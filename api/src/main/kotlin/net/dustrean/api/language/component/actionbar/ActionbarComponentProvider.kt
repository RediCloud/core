package net.dustrean.api.language.component.actionbar

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ActionbarComponentProvider : LanguageComponentProvider(LanguageType.ACTION_BAR) {
    var content = Component.empty()
}