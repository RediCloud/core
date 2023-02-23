package dev.redicloud.api.language.component.chat

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ChatComponentProvider : LanguageComponentProvider(LanguageType.CHAT_MESSAGE) {
    var message: Component = Component.empty()
}