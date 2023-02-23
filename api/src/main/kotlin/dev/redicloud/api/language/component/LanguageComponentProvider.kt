package dev.redicloud.api.language.component

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.placeholder.PlaceholderProvider

abstract class LanguageComponentProvider(val type: LanguageType){
    lateinit var key: String
    var placeholderProvider: PlaceholderProvider.() -> Unit = {}
}