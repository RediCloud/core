package net.dustrean.api.standalone.language

import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.book.BookComponent
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.bossbar.BossBarComponent
import net.dustrean.api.language.component.bossbar.BossBarComponentProvider
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title

class LanguageBridge : ILanguageBridge {
    override suspend fun sendMessage(
        player: ILanguagePlayer,
        provider: ChatComponentProvider,
        chatComponent: ChatComponent
    ): Component? {
        TODO("Not yet implemented")
    }

    override suspend fun sendTabList(
        player: ILanguagePlayer,
        provider: TabListComponentProvider,
        tabListComponent: TabListComponent
    ): Pair<Component, Component>? {
        TODO("Not yet implemented")
    }

    override suspend fun sendTitle(
        player: ILanguagePlayer,
        provider: TitleComponentProvider,
        titleComponent: TitleComponent
    ): Title? {
        TODO("Not yet implemented")
    }

    override suspend fun openBook(
        player: ILanguagePlayer,
        provider: BookComponentProvider,
        bookComponent: BookComponent
    ): Book? {
        TODO("Not yet implemented")
    }

    override suspend fun sendBossBar(
        player: ILanguagePlayer,
        provider: BossBarComponentProvider,
        bossBarComponent: BossBarComponent
    ): BossBar? {
        TODO("Not yet implemented")
    }

}