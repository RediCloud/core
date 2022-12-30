package net.dustrean.api.language.component.book

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class BookComponentProvider : LanguageComponentProvider(LanguageType.BOOK) {
    var titleComponent = Component.empty()
    var authorComponent = Component.empty()
    var pagesComponent = mutableListOf<Component>()
}