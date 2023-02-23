package dev.redicloud.api.language.component.actionbar

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ActionbarComponentProvider : LanguageComponentProvider(LanguageType.ACTION_BAR) {
    var content = Component.empty()
}