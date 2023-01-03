package net.dustrean.api.language.component.title

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

class TitleComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawTitle: String,
    val rawSubtitle: String,
    val fadeIn: Long,
    val stay: Long,
    val fadeOut: Long,
) : ILanguageComponent