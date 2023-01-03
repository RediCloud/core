package net.dustrean.api.language.component.text

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider

class TextComponentProvider : LanguageComponentProvider(LanguageType.TEXT) {
    var text: String = ""
}