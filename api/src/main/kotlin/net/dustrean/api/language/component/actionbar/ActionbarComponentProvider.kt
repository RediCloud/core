package net.dustrean.api.language.component.actionbar

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component
import kotlin.time.Duration.Companion.seconds

class ActionbarComponentProvider : LanguageComponentProvider(LanguageType.ACTION_BAR) {
    var content = Component.empty()
    var stay = 1.seconds.inWholeMilliseconds
}