package net.dustrean.api.language

import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentBuilder

interface ILanguageBridge {

    suspend fun sendMessage(player: ILanguagePlayer, provider: ChatComponentBuilder.LanguageChatComponentProvider, chatComponent: ChatComponent)

    suspend fun sendTabList(player: ILanguagePlayer, provider: TabListComponentBuilder.LanguageTabListComponentProvider, tabListComponent: TabListComponent)

}