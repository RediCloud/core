package dev.redicloud.api.player

import com.google.gson.annotations.Expose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.data.AbstractCacheHandler
import dev.redicloud.api.data.AbstractDataObject
import dev.redicloud.api.data.ICacheValidator
import dev.redicloud.api.language.ILanguageManager
import dev.redicloud.api.language.ILanguagePlayer
import dev.redicloud.api.language.LanguageManager
import dev.redicloud.api.language.component.book.BookComponentProvider
import dev.redicloud.api.language.component.bossbar.BossBarComponentProvider
import dev.redicloud.api.language.component.chat.ChatComponentProvider
import dev.redicloud.api.language.component.tablist.TabListComponentProvider
import dev.redicloud.api.language.component.title.TitleComponentProvider
import dev.redicloud.api.language.placeholder.Placeholder
import dev.redicloud.api.language.placeholder.collection.PlaceholderCollection
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.network.NetworkComponentType
import dev.redicloud.api.packet.connect.PlayerChangeServicePacket
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.lang.IllegalStateException
import java.time.Duration
import java.util.*

data class Player(
    override val uuid: UUID,
    override var name: String,
    override var coins: Long = 0,
    override var languageId: Int = ILanguageManager.DEFAULT_LANGUAGE_ID,
    override var connected: Boolean = false,
) : IPlayer, ILanguagePlayer, AbstractDataObject() {
    class PlayerCacheHandler(val lastServer: () -> NetworkComponentInfo) : AbstractCacheHandler(),
        ICacheValidator<AbstractDataObject> {

        override fun isValid(): Boolean = lastServer() == ICoreAPI.INSTANCE.networkComponentInfo
        override suspend fun getCacheNetworkComponents(): Set<NetworkComponentInfo> = setOf(lastServer())
    }

    companion object {
        val INVALID_ID = UUID(0, 0)
        val INVALID_SERVICE = NetworkComponentInfo(NetworkComponentType.STANDALONE, INVALID_ID)
        val defaultScope = CoroutineScope(Dispatchers.Default)
        const val INVALID_IP = "UNKNOWN"
    }

    override var lastServer: NetworkComponentInfo = INVALID_SERVICE
    override var lastProxy: NetworkComponentInfo = INVALID_SERVICE
    override var authentication: PlayerAuthentication = PlayerAuthentication()
    override val nameHistory: MutableList<Pair<Long, String>> = mutableListOf()
    override val sessions: MutableList<PlayerSession> = mutableListOf()

    @Expose(serialize = false, deserialize = false)
    override val placeholders = PlaceholderCollection("player").apply {
        add(Placeholder("name", { name }))
        add(Placeholder("uuid", { "$uuid" }))
        add(Placeholder("coins", { "$coins" }))
        add(
            Placeholder("language", {
                ICoreAPI.INSTANCE.languageManager.getLanguage(languageId)?.name
                    ?: ICoreAPI.INSTANCE.languageManager.getDefaultLanguage().name
            })
        )
    }

    @Expose(serialize = false, deserialize = false)
    private var playerCacheHandler: PlayerCacheHandler? = null
        get() = field ?: PlayerCacheHandler { lastServer }.also {
            playerCacheHandler = it
        }

    override val identifier: UUID
        get() = uuid

    override val cacheHandler
        get() = playerCacheHandler!!

    override val validator
        get() = playerCacheHandler!!

    override suspend fun update(): Player = ICoreAPI.getInstance<CoreAPI>().playerManager.updatePlayer(this)

    override fun getCurrentSession(): PlayerSession? {
        val session = sessions.lastOrNull() ?: return null
        return if (session.isActive()) session else null
    }

    override fun getLastSession(): PlayerSession? {
        return sessions.lastOrNull { !it.isActive() }
    }

    override fun isOnCurrent(): Boolean = when (ICoreAPI.INSTANCE.networkComponentInfo.type) {
        NetworkComponentType.STANDALONE -> connected
        NetworkComponentType.VELOCITY -> lastProxy == ICoreAPI.INSTANCE.networkComponentInfo
        NetworkComponentType.MINESTOM -> lastServer == ICoreAPI.INSTANCE.networkComponentInfo
        NetworkComponentType.PAPER -> lastServer == ICoreAPI.INSTANCE.networkComponentInfo
    }

    override suspend fun connect(service: NetworkComponentInfo) {
        if (!connected) return
        val packet = PlayerChangeServicePacket().apply {
            this.uniqueId = uuid
            this.networkComponentInfo = service
        }
        packet.sendPacket()
    }

    override fun sendMessage(provider: ChatComponentProvider.() -> Unit): Deferred<Component> = defaultScope.async {
        val built = ChatComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.languageManager.getChatMessage(languageId, built)
        return@async ICoreAPI.INSTANCE.languageBridge.sendMessage(this@Player, built, component) ?:
        throw IllegalStateException("Player is not connected")
    }

    override fun sendTabList(provider: TabListComponentProvider.() -> Unit): Deferred<Pair<Component, Component>> =
        defaultScope.async {
            val built = TabListComponentProvider().apply(provider)
            if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
            val component = ICoreAPI.INSTANCE.languageManager.getTabList(languageId, built)
            return@async ICoreAPI.INSTANCE.languageBridge.sendTabList(this@Player, built, component) ?:
            throw IllegalStateException("Player is not connected")
    }

    override fun sendTitle(provider: TitleComponentProvider.() -> Unit, fadeIn: Duration, stay: Duration, fadeOut: Duration): Deferred<Title> = defaultScope.async {
        val built = TitleComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.languageManager.getTitle(languageId, built)
        return@async ICoreAPI.INSTANCE.languageBridge.sendTitle(this@Player, built, component, fadeIn, stay, fadeOut) ?:
        throw IllegalStateException("Player is not connected")
    }

    override fun openBook(provider: BookComponentProvider.() -> Unit): Deferred<Book> = defaultScope.async {
        val built = BookComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.languageManager.getBook(languageId, built)
        return@async ICoreAPI.INSTANCE.languageBridge.openBook(this@Player, built, component) ?:
        throw IllegalStateException("Player is not connected")
    }

    override fun sendBossBar(provider: BossBarComponentProvider.() -> Unit, overlay: BossBar.Overlay, color: BossBar.Color, progress: Float): Deferred<BossBar> = defaultScope.async {
        val built = BossBarComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.languageManager.getBossBar(languageId, built)
        return@async ICoreAPI.INSTANCE.languageBridge.sendBossBar(this@Player, built, component, overlay, color, progress) ?:
        throw IllegalStateException("Player is not connected")
    }

    override fun getPlaceholders(prefix: String): PlaceholderCollection {
        if (prefix == "player") return placeholders
        return placeholders.copy(prefix)
    }

}