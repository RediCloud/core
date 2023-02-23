package dev.redicloud.api.language.component.tablist

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.ILanguageComponent

class TabListComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType = LanguageType.TAB_LIST,
    override val serializerType: LanguageSerializerType,
    val rawHeader: String,
    val rawFooter: String,
) : ILanguageComponent