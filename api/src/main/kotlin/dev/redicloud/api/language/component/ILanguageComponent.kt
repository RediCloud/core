package dev.redicloud.api.language.component

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType

interface ILanguageComponent {
    val key: String
    val languageId: Int
    val type: LanguageType
    val serializerType: LanguageSerializerType
}