package net.dustrean.api.language.component.sign

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class SignComponentProvider : LanguageComponentProvider(LanguageType.SIGN) {
    var line1 = Component.empty()
    var line2 = Component.empty()
    var line3 = Component.empty()
    var line4 = Component.empty()
}