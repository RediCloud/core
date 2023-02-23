package dev.redicloud.api.language

import dev.redicloud.api.language.component.book.BookComponent
import dev.redicloud.api.language.component.book.BookComponentProvider
import dev.redicloud.api.language.component.bossbar.BossBarComponent
import dev.redicloud.api.language.component.bossbar.BossBarComponentProvider
import dev.redicloud.api.language.component.chat.ChatComponent
import dev.redicloud.api.language.component.chat.ChatComponentProvider
import dev.redicloud.api.language.component.tablist.TabListComponent
import dev.redicloud.api.language.component.tablist.TabListComponentProvider
import dev.redicloud.api.language.component.title.TitleComponent
import dev.redicloud.api.language.component.title.TitleComponentProvider
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration

interface ILanguageBridge {

    suspend fun sendMessage(player: ILanguagePlayer, provider: ChatComponentProvider, chatComponent: ChatComponent): Component?

    suspend fun sendTabList(player: ILanguagePlayer, provider: TabListComponentProvider, tabListComponent: TabListComponent): Pair<Component, Component>?

    suspend fun sendTitle(player: ILanguagePlayer, provider: TitleComponentProvider, titleComponent: TitleComponent, fadeIn: Duration, stay: Duration, fadeOut: Duration): Title?

    suspend fun openBook(player: ILanguagePlayer, provider: BookComponentProvider, bookComponent: BookComponent): Book?

    suspend fun sendBossBar(player: ILanguagePlayer, provider: BossBarComponentProvider, bossBarComponent: BossBarComponent, overlay: BossBar.Overlay, color: BossBar.Color, progress: Float): BossBar?

}