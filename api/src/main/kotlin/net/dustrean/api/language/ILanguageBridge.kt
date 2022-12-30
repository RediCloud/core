package net.dustrean.api.language

import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.player.IPlayer

interface ILanguageBridge {

    suspend fun sendMessage(player: ILanguagePlayer, provider: ChatComponentBuilder.LanguageChatComponentProvider, chatComponent: ChatComponent)

}