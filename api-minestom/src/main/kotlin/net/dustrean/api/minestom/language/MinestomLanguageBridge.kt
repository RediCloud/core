package net.dustrean.api.minestom.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.language.CloudLanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.book.BookComponent
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.inventory.Book
import net.minestom.server.MinecraftServer

class MinestomLanguageBridge : CloudLanguageBridge() {

    override suspend fun sendTabList(
        player: ILanguagePlayer, provider: TabListComponentProvider, tabListComponent: TabListComponent
    ) {
        val minestomPlayer = MinecraftServer.getConnectionManager().getPlayer(player.uuid) ?: return
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val header = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawHeaderComponent,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawHeaderComponent)
        )
        val footer = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawFooterComponent,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawFooterComponent)
        )
        minestomPlayer.sendPlayerListHeaderAndFooter(header, footer)
    }

    override suspend fun openBook(
        player: ILanguagePlayer,
        provider: BookComponentProvider,
        bookComponent: BookComponent
    ) {
        val minestomPlayer = MinecraftServer.getConnectionManager().getPlayer(player.uuid) ?: return
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val book = Book.book(
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                bookComponent.rawTitleComponent,
                bookComponent.serializerType,
                placeholderProvider.parse(bookComponent.rawTitleComponent)
            ),
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                bookComponent.rawAuthorComponent,
                bookComponent.serializerType,
                placeholderProvider.parse(bookComponent.rawAuthorComponent)
            ),
            *bookComponent.rawPagesComponent.map { page ->
                ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                    page,
                    bookComponent.serializerType,
                    placeholderProvider.parse(page)
                )
            }.toTypedArray()
        )
        minestomPlayer.openBook(book)
    }

}