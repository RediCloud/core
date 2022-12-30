package net.dustrean.api.standalone.language

import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentBuilder

class LanguageBridge : ILanguageBridge {
    override suspend fun sendMessage(
        player: ILanguagePlayer,
        provider: ChatComponentBuilder.LanguageChatComponentProvider,
        chatComponent: ChatComponent
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun sendTabList(
        player: ILanguagePlayer,
        provider: TabListComponentBuilder.LanguageTabListComponentProvider,
        tabListComponent: TabListComponent
    ) {
        TODO("Not yet implemented")
    }
}