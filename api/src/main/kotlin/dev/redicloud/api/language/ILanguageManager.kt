package dev.redicloud.api.language

import dev.redicloud.api.language.component.actionbar.ActionbarComponent
import dev.redicloud.api.language.component.actionbar.ActionbarComponentProvider
import dev.redicloud.api.language.component.book.BookComponent
import dev.redicloud.api.language.component.book.BookComponentProvider
import dev.redicloud.api.language.component.bossbar.BossBarComponent
import dev.redicloud.api.language.component.bossbar.BossBarComponentProvider
import dev.redicloud.api.language.component.chat.ChatComponent
import dev.redicloud.api.language.component.chat.ChatComponentProvider
import dev.redicloud.api.language.component.inventory.InventoryComponent
import dev.redicloud.api.language.component.inventory.InventoryComponentProvider
import dev.redicloud.api.language.component.item.ItemComponent
import dev.redicloud.api.language.component.item.ItemComponentProvider
import dev.redicloud.api.language.component.scoreboard.ScoreboardLineComponent
import dev.redicloud.api.language.component.scoreboard.ScoreboardLineComponentProvider
import dev.redicloud.api.language.component.sign.SignComponent
import dev.redicloud.api.language.component.sign.SignComponentProvider
import dev.redicloud.api.language.component.tablist.TabListComponent
import dev.redicloud.api.language.component.tablist.TabListComponentProvider
import dev.redicloud.api.language.component.text.TextComponent
import dev.redicloud.api.language.component.text.TextComponentProvider
import dev.redicloud.api.language.component.title.TitleComponent
import dev.redicloud.api.language.component.title.TitleComponentProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

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

    fun deserialize(input: String, type: LanguageSerializerType, vararg tagResolvers: TagResolver): Component
    fun serialize(component: Component, type: LanguageSerializerType): String

}