package net.dustrean.api.language.component.book

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class BookComponentProvider : LanguageComponentProvider(LanguageType.BOOK) {
    var title = Component.empty()
    var author = Component.empty()
    var pages = mutableListOf<Component>()
}