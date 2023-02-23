package dev.redicloud.api.language.component.text

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider

class TextComponentProvider : LanguageComponentProvider(LanguageType.TEXT) {
    var text: String = ""
}