package net.dustrean.api.language

import net.dustrean.api.language.component.actionbar.ActionbarComponent
import net.dustrean.api.language.component.actionbar.ActionbarComponentProvider
import net.dustrean.api.language.component.book.BookComponent
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.bossbar.BossBarComponent
import net.dustrean.api.language.component.bossbar.BossBarComponentProvider
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.inventory.InventoryComponent
import net.dustrean.api.language.component.inventory.InventoryComponentProvider
import net.dustrean.api.language.component.item.ItemComponent
import net.dustrean.api.language.component.item.ItemComponentProvider
import net.dustrean.api.language.component.scoreboard.ScoreboardLineComponent
import net.dustrean.api.language.component.scoreboard.ScoreboardLineComponentProvider
import net.dustrean.api.language.component.sign.SignComponent
import net.dustrean.api.language.component.sign.SignComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.text.TextComponent
import net.dustrean.api.language.component.text.TextComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider

interface ILanguageManager {

    companion object {
        const val DEFAULT_LANGUAGE_ID = 0
        const val DEFAULT_PRIMARY_COLOR = "#3ABFF8"
        const val DEFAULT_SECONDARY_COLOR = "#3b82f6"
        val DEFAULT_SERIALIZER_TYPE = LanguageSerializerType.MINI_MESSAGES
    }

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

    suspend fun getBook(
        languageId: Int, provider: BookComponentProvider
    ): BookComponent

    suspend fun getBossBar(
        languageId: Int, provider: BossBarComponentProvider
    ): BossBarComponent

    suspend fun getScoreboardLine(
        languageId: Int, provider: ScoreboardLineComponentProvider
    ): ScoreboardLineComponent

    suspend fun getItem(
        languageId: Int, provider: ItemComponentProvider
    ): ItemComponent

    suspend fun getText(
        languageId: Int, provider: TextComponentProvider
    ): TextComponent

    suspend fun getActionbar(
        languageId: Int, provider: ActionbarComponentProvider
    ): ActionbarComponent

    suspend fun getSign(
        languageId: Int, provider: SignComponentProvider
    ): SignComponent

    suspend fun getInventory(
        languageId: Int, provider: InventoryComponentProvider
    ): InventoryComponent

}