package net.dustrean.api.language.component

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.placeholder.PlaceholderProvider

abstract class LanguageComponentProvider(val type: LanguageType){
    lateinit var key: String
    var placeholderProvider: PlaceholderProvider.() -> Unit = {}
}