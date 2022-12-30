package net.dustrean.api.paper.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.language.CloudLanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.book.BookComponent
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.bossbar.BossBarComponent
import net.dustrean.api.language.component.bossbar.BossBarComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import org.bukkit.Bukkit

class PaperLanguageBridge : CloudLanguageBridge() {

    override suspend fun sendTabList(
        player: ILanguagePlayer,
        provider: TabListComponentProvider,
        tabListComponent: TabListComponent
    ) {
        val paperPlayer = Bukkit.getPlayer(player.uuid) ?: return
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val header = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawHeader,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawHeader)
        )
        val footer = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawFooter,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawFooter)
        )
        paperPlayer.sendPlayerListHeaderAndFooter(header, footer)
    }

    override suspend fun openBook(
        player: ILanguagePlayer,
        provider: BookComponentProvider,
        bookComponent: BookComponent
    ) {
        val paperPlayer = Bukkit.getPlayer(player.uuid) ?: return
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val book = Book.book(
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                bookComponent.rawTitle,
                bookComponent.serializerType,
                placeholderProvider.parse(bookComponent.rawTitle)
            ),
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                bookComponent.rawAuthor,
                bookComponent.serializerType,
                placeholderProvider.parse(bookComponent.rawAuthor)
            ),
            *bookComponent.rawPages.map { page ->
                ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                    page,
                    bookComponent.serializerType,
                    placeholderProvider.parse(page)
                )
            }.toTypedArray()
        )
        paperPlayer.openBook(book)
    }

    override suspend fun sendBossBar(
        player: ILanguagePlayer,
        provider: BossBarComponentProvider,
        bossBarComponent: BossBarComponent
    ) {
        val paperPlayer = Bukkit.getPlayer(player.uuid) ?: return
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val bossBar = BossBar.bossBar(
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                bossBarComponent.rawName,
                bossBarComponent.serializerType,
                placeholderProvider.parse(bossBarComponent.rawName)
            ),
            bossBarComponent.progress,
            bossBarComponent.color,
            bossBarComponent.overlay
        )
        //TODO: show/hide
        paperPlayer.showBossBar(bossBar)
    }

}