package net.dustrean.api.language.component.scoreboard

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ScoreboardLineComponentProvider : LanguageComponentProvider(LanguageType.SCOREBOARD) {
    var content = Component.empty()
}