package net.dustrean.api.language

import kotlinx.coroutines.Deferred
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.bossbar.BossBarComponentProvider
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.util.*

interface ILanguagePlayer {

    val uuid: UUID
    var languageId: Int
    val placeholders: PlaceholderCollection

    fun sendMessage(provider: ChatComponentProvider.() -> Unit): Deferred<Component>

    fun sendTabList(provider: TabListComponentProvider.() -> Unit): Deferred<Pair<Component, Component>>

    fun sendTitle(provider: TitleComponentProvider.() -> Unit): Deferred<Title>

    fun openBook(provider: BookComponentProvider.() -> Unit): Deferred<Book>

    fun sendBossBar(provider: BossBarComponentProvider.() -> Unit): Deferred<BossBar>

    fun getPlaceholders(prefix: String = ""): PlaceholderCollection

}