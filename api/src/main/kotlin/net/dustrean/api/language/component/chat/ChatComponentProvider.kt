package net.dustrean.api.language.component.chat

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ChatComponentProvider : LanguageComponentProvider(LanguageType.CHAT_MESSAGE) {
    var message: Component = Component.empty()
}