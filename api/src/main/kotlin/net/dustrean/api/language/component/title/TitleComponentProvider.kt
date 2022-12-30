package net.dustrean.api.language.component.title

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import kotlin.time.Duration.Companion.seconds

class TitleComponentProvider : LanguageComponentProvider(LanguageType.TITLE) {
    var title = Component.empty()
    var subtitle = Component.empty()
    var fadeIn = 0.5.seconds.inWholeMilliseconds
    var stay = 1.seconds.inWholeMilliseconds
    var fadeOut = 0.5.seconds.inWholeMilliseconds
}