package net.dustrean.api.language.component.book

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

class BookComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawTitleComponent: String,
    val rawAuthorComponent: String,
    val rawPagesComponent: List<String>,
) : ILanguageComponent