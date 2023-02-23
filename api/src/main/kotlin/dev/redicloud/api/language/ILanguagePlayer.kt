package dev.redicloud.api.language

import kotlinx.coroutines.Deferred
import dev.redicloud.api.language.component.book.BookComponentProvider
import dev.redicloud.api.language.component.bossbar.BossBarComponentProvider
import dev.redicloud.api.language.component.chat.ChatComponentProvider
import dev.redicloud.api.language.component.tablist.TabListComponentProvider
import dev.redicloud.api.language.component.title.TitleComponentProvider
import dev.redicloud.api.language.placeholder.collection.PlaceholderCollection
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration
import java.util.*

interface ILanguagePlayer {

    val uuid: UUID
    var languageId: Int
    val placeholders: PlaceholderCollection

    fun sendMessage(provider: ChatComponentProvider.() -> Unit): Deferred<Component>

    fun sendTabList(provider: TabListComponentProvider.() -> Unit): Deferred<Pair<Component, Component>>

    fun sendTitle(provider: TitleComponentProvider.() -> Unit, fadeIn: Duration, stay: Duration, fadeOut: Duration): Deferred<Title>

    fun openBook(provider: BookComponentProvider.() -> Unit): Deferred<Book>

    fun sendBossBar(provider: BossBarComponentProvider.() -> Unit, overlay: BossBar.Overlay, color: BossBar.Color, progress: Float): Deferred<BossBar>

    fun getPlaceholders(prefix: String = ""): PlaceholderCollection

}