package net.dustrean.api.language.component.chat

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

class ChatComponent(
    override val key: String,
    override val type: LanguageType = LanguageType.CHAT_MESSAGE,
    override val languageId: Int,
    override val serializerType: LanguageSerializerType,
    val rawComponent: String,
) : ILanguageComponent