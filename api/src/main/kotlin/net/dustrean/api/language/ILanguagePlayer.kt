package net.dustrean.api.language

import kotlinx.coroutines.Deferred
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.component.tablist.TabListComponentBuilder
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import java.util.*

interface ILanguagePlayer {

    val uuid: UUID
    var languageId: Int
    val placeholders: PlaceholderCollection

    fun sendMessage(provider: ChatComponentBuilder.LanguageChatComponentProvider): Deferred<Unit>

    fun sendTabList(provider: TabListComponentBuilder.LanguageTabListComponentProvider): Deferred<Unit>

    fun getPlaceholders(prefix: String = ""): PlaceholderCollection

}