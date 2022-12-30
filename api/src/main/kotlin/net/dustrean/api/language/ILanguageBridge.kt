package net.dustrean.api.language

import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider

interface ILanguageBridge {

    suspend fun sendMessage(player: ILanguagePlayer, provider: ChatComponentProvider, chatComponent: ChatComponent)

    suspend fun sendTabList(player: ILanguagePlayer, provider: TabListComponentProvider, tabListComponent: TabListComponent)

    suspend fun sendTitle(player: ILanguagePlayer, provider: TitleComponentProvider, titleComponent: TitleComponent)

}