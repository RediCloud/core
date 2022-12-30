package net.dustrean.api.language

import kotlinx.coroutines.Deferred
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.bossbar.BossBarComponentProvider
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import java.util.*

interface ILanguagePlayer {

    val uuid: UUID
    var languageId: Int
    val placeholders: PlaceholderCollection

    fun sendMessage(provider: ChatComponentProvider.() -> Unit): Deferred<Unit>

    fun sendTabList(provider: TabListComponentProvider.() -> Unit): Deferred<Unit>

    fun sendTitle(provider: TitleComponentProvider.() -> Unit): Deferred<Unit>

    fun openBook(provider: BookComponentProvider.() -> Unit): Deferred<Unit>

    fun getPlaceholders(prefix: String = ""): PlaceholderCollection

}