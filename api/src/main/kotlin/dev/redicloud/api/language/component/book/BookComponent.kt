package dev.redicloud.api.language.component.book

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.ILanguageComponent

class BookComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawTitle: String,
    val rawAuthor: String,
    val rawPages: List<String>,
) : ILanguageComponent