package dev.redicloud.api.language.component.chat

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.ILanguageComponent

class ChatComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType = LanguageType.CHAT_MESSAGE,
    override val serializerType: LanguageSerializerType,
    val rawMessage: String
) : ILanguageComponent