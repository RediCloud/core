package dev.redicloud.api.minestom.language

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.cloud.language.CloudLanguageBridge
import dev.redicloud.api.language.ILanguagePlayer
import dev.redicloud.api.language.component.book.BookComponent
import dev.redicloud.api.language.component.book.BookComponentProvider
import dev.redicloud.api.language.component.bossbar.BossBarComponent
import dev.redicloud.api.language.component.bossbar.BossBarComponentProvider
import dev.redicloud.api.language.component.tablist.TabListComponent
import dev.redicloud.api.language.component.tablist.TabListComponentProvider
import dev.redicloud.api.language.component.title.TitleComponent
import dev.redicloud.api.language.component.title.TitleComponentProvider
import dev.redicloud.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.minestom.server.MinecraftServer
import java.time.Duration

class MinestomLanguageBridge : CloudLanguageBridge() {
    override suspend fun sendTitle(
        player: ILanguagePlayer,
        provider: TitleComponentProvider,
        titleComponent: TitleComponent,
        fadeIn: Duration,
        stay: Duration,
        fadeOut: Duration
    ): Title? {
        val minestomPlayer = MinecraftServer.getConnectionManager().getPlayer(player.uuid) ?: return null
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val title = Title.title(
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                titleComponent.rawTitle,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawTitle)
            ),
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                titleComponent.rawSubtitle,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawSubtitle)
            ),
            Title.Times.times(fadeIn, stay, fadeOut)
        )
        minestomPlayer.showTitle(title)
        return title
    }

    override suspend fun sendTabList(
        player: ILanguagePlayer, provider: TabListComponentProvider, tabListComponent: TabListComponent
    ): Pair<Component, Component>? {
        val minestomPlayer = MinecraftServer.getConnectionManager().getPlayer(player.uuid) ?: return null
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val header = ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
            tabListComponent.rawHeader,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawHeader)
        )
        val footer = ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
            tabListComponent.rawFooter,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawFooter)
        )
        minestomPlayer.sendPlayerListHeaderAndFooter(header, footer)
        return header to footer
    }

    override suspend fun openBook(
        player: ILanguagePlayer,
        provider: BookComponentProvider,
        bookComponent: BookComponent
    ): Book? {
        val minestomPlayer = MinecraftServer.getConnectionManager().getPlayer(player.uuid) ?: return null
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val book = Book.book(
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                bookComponent.rawTitle,
                bookComponent.serializerType,
                placeholderProvider.parse(bookComponent.rawTitle)
            ),
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                bookComponent.rawAuthor,
                bookComponent.serializerType,
                placeholderProvider.parse(bookComponent.rawAuthor)
            ),
            *bookComponent.rawPages.map { page ->
                ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                    page,
                    bookComponent.serializerType,
                    placeholderProvider.parse(page)
                )
            }.toTypedArray()
        )
        minestomPlayer.openBook(book)
        return book
    }

    override suspend fun sendBossBar(
        player: ILanguagePlayer,
        provider: BossBarComponentProvider,
        bossBarComponent: BossBarComponent,
        overlay: BossBar.Overlay,
        color: BossBar.Color,
        progress: Float
    ): BossBar? {
        val minestomPlayer = MinecraftServer.getConnectionManager().getPlayer(player.uuid) ?: return null
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val bossBar = BossBar.bossBar(
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                bossBarComponent.rawName,
                bossBarComponent.serializerType,
                placeholderProvider.parse(bossBarComponent.rawName)
            ),
            progress,
            color,
            overlay
        )
        minestomPlayer.showBossBar(bossBar)
        return bossBar
    }

}