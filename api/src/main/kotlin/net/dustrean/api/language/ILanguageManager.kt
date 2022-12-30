package net.dustrean.api.language

import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentBuilder

interface ILanguageManager {

    suspend fun getLanguage(languageId: Int): Language?
    suspend fun getDefaultLanguage(): Language

    suspend fun getChatMessage(
        languageId: Int, provider: ChatComponentBuilder.LanguageChatComponentProvider
    ): ChatComponent

    suspend fun getTabList(
        languageId: Int, provider: TabListComponentBuilder.LanguageTabListComponentProvider
    ): TabListComponent

}