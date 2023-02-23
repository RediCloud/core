package dev.redicloud.api.language.component.sign

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.ILanguageComponent

class SignComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawLine1: String,
    val rawLine2: String,
    val rawLine3: String,
    val rawLine4: String
) : ILanguageComponent