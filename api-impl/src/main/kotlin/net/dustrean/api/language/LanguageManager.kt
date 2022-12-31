package net.dustrean.api.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.language.component.ILanguageComponent
import net.dustrean.api.language.component.book.BookComponent
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.bossbar.BossBarComponent
import net.dustrean.api.language.component.bossbar.BossBarComponentProvider
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.item.ItemComponent
import net.dustrean.api.language.component.item.ItemComponentProvider
import net.dustrean.api.language.component.scoreboard.ScoreboardLineComponent
import net.dustrean.api.language.component.scoreboard.ScoreboardLineComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.text.TextComponent
import net.dustrean.api.language.component.text.TextComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RLocalCachedMap

class LanguageManager(core: CoreAPI) : ILanguageManager {

    companion object {
        const val DEFAULT_LANGUAGE_ID = 0
        const val DEFAULT_PRIMARY_COLOR = "#3ABFF8"
        const val DEFAULT_SECONDARY_COLOR = "#3b82f6"
        val DEFAULT_SERIALIZER_TYPE = LanguageSerializerType.MINI_MESSAGES
    }

    private val languages: RLocalCachedMap<Int, Language> = core.getRedisConnection().redisClient.getLocalCachedMap(
        "language:languages",
        LocalCachedMapOptions.defaults<Int?, Language?>().storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
    )
    private val componentMaps = mutableMapOf<LanguageType, RLocalCachedMap<String, ILanguageComponent>>()

    init {
        if (!languages.containsKey(DEFAULT_LANGUAGE_ID)) {
            languages[DEFAULT_LANGUAGE_ID] = Language(
                DEFAULT_LANGUAGE_ID,
                "English",
                "http://textures.minecraft.net/texture/879d99d9c46474e2713a7e84a95e4ce7e8ff8ea4d164413a592e4435d2c6f9dc"
            )
        }
        LanguageType.values().forEach {
            componentMaps[it] = core.getRedisConnection().redisClient.getLocalCachedMap(
                "language:${it.name.lowercase()}",
                LocalCachedMapOptions.defaults<String?, ILanguageComponent?>()
                    .storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
                    .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
            )
        }
    }

    override suspend fun getLanguage(languageId: Int): Language? {
        return languages[languageId]
    }

    override suspend fun getDefaultLanguage(): Language {
        return languages[DEFAULT_LANGUAGE_ID]!!
    }

    override suspend fun getChatMessage(
        languageId: Int, provider: ChatComponentProvider
    ): ChatComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = ChatComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                serialize(provider.message, DEFAULT_SERIALIZER_TYPE),
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as ChatComponent
    }

    override suspend fun getTabList(
        languageId: Int, provider: TabListComponentProvider
    ): TabListComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = TabListComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                serialize(provider.header, DEFAULT_SERIALIZER_TYPE),
                serialize(provider.footer, DEFAULT_SERIALIZER_TYPE),
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as TabListComponent
    }

    override suspend fun getBook(languageId: Int, provider: BookComponentProvider): BookComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = BookComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                serialize(provider.title, DEFAULT_SERIALIZER_TYPE),
                serialize(provider.author, DEFAULT_SERIALIZER_TYPE),
                provider.pages.map {
                    serialize(it, DEFAULT_SERIALIZER_TYPE)
                }.toTypedArray().toList()
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as BookComponent
    }

    override suspend fun getTitle(
        languageId: Int, provider: TitleComponentProvider
    ): TitleComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = TitleComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                serialize(provider.title, DEFAULT_SERIALIZER_TYPE),
                serialize(provider.subtitle, DEFAULT_SERIALIZER_TYPE),
                provider.fadeIn,
                provider.stay,
                provider.fadeOut
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as TitleComponent
    }

    override suspend fun getBossBar(languageId: Int, provider: BossBarComponentProvider): BossBarComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = BossBarComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                serialize(provider.name, DEFAULT_SERIALIZER_TYPE),
                provider.progress,
                provider.color,
                provider.overlay
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as BossBarComponent
    }

    override suspend fun getScoreboardLine(
        languageId: Int,
        provider: ScoreboardLineComponentProvider
    ): ScoreboardLineComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = ScoreboardLineComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                serialize(provider.content, DEFAULT_SERIALIZER_TYPE),
                provider.id,
                provider.score,
                provider.lineType
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as ScoreboardLineComponent
    }

    override suspend fun getItem(
        languageId: Int,
        provider: ItemComponentProvider
    ): ItemComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = ItemComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                provider.material.name,
                serialize(provider.name, DEFAULT_SERIALIZER_TYPE),
                provider.amount,
                provider.damage,
                provider.lore.map {
                    serialize(it, DEFAULT_SERIALIZER_TYPE)
                }.toTypedArray().toList(),
                provider.unbreakable,
                provider.blockDrop,
                provider.blockInteract,
                provider.blockClick,
                provider.permission,
                provider.skullOwner,
                provider.skullTexture,
                provider.properties
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as ItemComponent
    }

    override suspend fun getText(languageId: Int, provider: TextComponentProvider): TextComponent {
        val language = getLanguage(languageId) ?: getDefaultLanguage()
        val components = getMap(provider.type)
        if (!components.containsKey(provider.key)) {
            val fallbackComponent = TextComponent(
                provider.key,
                language.id,
                provider.type,
                DEFAULT_SERIALIZER_TYPE,
                provider.text
            )
            components[provider.key] = fallbackComponent
            return fallbackComponent
        }
        return components[provider.key] as TextComponent
    }

    private fun getMap(type: LanguageType): RLocalCachedMap<String, ILanguageComponent> {
        return componentMaps[type]!!
    }

    fun deserialize(input: String, type: LanguageSerializerType, vararg tagResolvers: TagResolver): Component =
        when (type) {
            LanguageSerializerType.MINI_MESSAGES -> MiniMessage.miniMessage().deserialize(input, *tagResolvers)
            LanguageSerializerType.GSON -> GsonComponentSerializer.gson().deserialize(input)
            LanguageSerializerType.LEGACY_SECTION -> LegacyComponentSerializer.legacySection().deserialize(input)
            LanguageSerializerType.LEGACY_AMPERSAND -> LegacyComponentSerializer.legacyAmpersand().deserialize(input)
            LanguageSerializerType.GSON_COLOR_DOWN_SAMPLING -> GsonComponentSerializer.colorDownsamplingGson()
                .deserialize(input)

            else -> Component.text(input)
        }

    fun serialize(component: Component, type: LanguageSerializerType): String = when (type) {
        LanguageSerializerType.MINI_MESSAGES -> MiniMessage.miniMessage().serialize(component)
        LanguageSerializerType.GSON -> GsonComponentSerializer.gson().serialize(component)
        LanguageSerializerType.LEGACY_SECTION -> LegacyComponentSerializer.legacySection().serialize(component)
        LanguageSerializerType.LEGACY_AMPERSAND -> LegacyComponentSerializer.legacyAmpersand().serialize(component)
        LanguageSerializerType.GSON_COLOR_DOWN_SAMPLING -> GsonComponentSerializer.colorDownsamplingGson()
            .serialize(component)

        else -> component.toString()
    }

}