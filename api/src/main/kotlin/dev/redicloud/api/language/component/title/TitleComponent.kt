package dev.redicloud.api.language.component.title

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.ILanguageComponent

class TitleComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawTitle: String,
    val rawSubtitle: String
) : ILanguageComponent