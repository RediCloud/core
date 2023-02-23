package dev.redicloud.api.language.component.title

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import kotlin.time.Duration.Companion.seconds

class TitleComponentProvider : LanguageComponentProvider(LanguageType.TITLE) {
    var title = Component.empty()
    var subtitle = Component.empty()
}