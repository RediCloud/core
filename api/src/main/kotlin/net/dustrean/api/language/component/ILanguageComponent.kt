package net.dustrean.api.language.component

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType

interface ILanguageComponent {

    val key: String
    val type: LanguageType
    val languageId: Int
    val serializerType: LanguageSerializerType

}