package net.dustrean.api.language.component.tablist

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

class TabListComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType = LanguageType.TAB_LIST,
    override val serializerType: LanguageSerializerType,
    val rawHeader: String,
    val rawFooter: String,
) : ILanguageComponent