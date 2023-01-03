package net.dustrean.api.language.component.sign

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

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