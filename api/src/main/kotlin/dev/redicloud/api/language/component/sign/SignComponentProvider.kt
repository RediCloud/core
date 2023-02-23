package dev.redicloud.api.language.component.sign

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class SignComponentProvider : LanguageComponentProvider(LanguageType.SIGN) {
    var line1 = Component.empty()
    var line2 = Component.empty()
    var line3 = Component.empty()
    var line4 = Component.empty()
}