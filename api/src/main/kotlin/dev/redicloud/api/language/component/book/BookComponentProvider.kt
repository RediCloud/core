package dev.redicloud.api.language.component.book

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class BookComponentProvider : LanguageComponentProvider(LanguageType.BOOK) {
    var title = Component.empty()
    var author = Component.empty()
    var pages = mutableListOf<Component>()
}