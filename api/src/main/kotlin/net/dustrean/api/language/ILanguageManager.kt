package net.dustrean.api.language

import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider

interface ILanguageManager {

    suspend fun getLanguage(languageId: Int): Language?
    suspend fun getDefaultLanguage(): Language

    suspend fun getChatMessage(
        languageId: Int, provider: ChatComponentProvider
    ): ChatComponent

    suspend fun getTabList(
        languageId: Int, provider: TabListComponentProvider
    ): TabListComponent

    suspend fun getTitle(
        languageId: Int, provider: TitleComponentProvider
    ): TitleComponent

}