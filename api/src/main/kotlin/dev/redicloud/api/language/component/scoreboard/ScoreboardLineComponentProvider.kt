package dev.redicloud.api.language.component.scoreboard

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ScoreboardLineComponentProvider : LanguageComponentProvider(LanguageType.SCOREBOARD) {
    var content = Component.empty()
}