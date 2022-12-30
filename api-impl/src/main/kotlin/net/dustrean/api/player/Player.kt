package net.dustrean.api.player

import com.google.gson.annotations.Expose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.data.ICacheValidator
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.LanguageManager
import net.dustrean.api.language.component.book.BookComponentProvider
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.packet.connect.PlayerChangeServicePacket
import java.lang.IllegalArgumentException
import java.util.*

data class Player(
    override val uuid: UUID,
    override var name: String,
    override var coins: Long = 0,
    override var languageId: Int = LanguageManager.DEFAULT_LANGUAGE_ID,
    override var connected: Boolean = false,
) : IPlayer, ILanguagePlayer, AbstractDataObject() {
    class PlayerCacheHandler(val lastServer: () -> NetworkComponentInfo) : AbstractCacheHandler(),
        ICacheValidator<AbstractDataObject> {

        override fun isValid(): Boolean = lastServer() == ICoreAPI.INSTANCE.getNetworkComponentInfo()
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
    override val placeholders = PlaceholderCollection("player")

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

    override suspend fun update(): Player = ICoreAPI.getInstance<CoreAPI>().getPlayerManager().updatePlayer(this)

    override fun getCurrentSession(): PlayerSession? {
        val session = sessions.lastOrNull() ?: return null
        return if (session.isActive()) session else null
    }

    override fun getLastSession(): PlayerSession? {
        return sessions.lastOrNull { !it.isActive() }
    }

    override fun isOnCurrent(): Boolean = when (ICoreAPI.INSTANCE.getNetworkComponentInfo().type) {
        NetworkComponentType.STANDALONE -> connected
        NetworkComponentType.VELOCITY -> lastProxy == ICoreAPI.INSTANCE.getNetworkComponentInfo()
        NetworkComponentType.MINESTOM -> lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
        NetworkComponentType.PAPER -> lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
    }

    override suspend fun connect(service: NetworkComponentInfo) {
        if (!connected) return
        val packet = PlayerChangeServicePacket().apply {
            this.uniqueId = uuid
            this.networkComponentInfo = service
        }
        packet.sendPacket()
    }

    override fun sendMessage(provider: ChatComponentProvider.() -> Unit): Deferred<Unit> = defaultScope.async {
        val built = ChatComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.getLanguageManager().getChatMessage(languageId, built)
        ICoreAPI.INSTANCE.getLanguageBridge().sendMessage(this@Player, built, component)
    }

    override fun sendTabList(provider: TabListComponentProvider.() -> Unit): Deferred<Unit> = defaultScope.async {
        val built = TabListComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.getLanguageManager().getTabList(languageId, built)
        ICoreAPI.INSTANCE.getLanguageBridge().sendTabList(this@Player, built, component)
    }

    override fun sendTitle(provider: TitleComponentProvider.() -> Unit): Deferred<Unit> = defaultScope.async {
        val built = TitleComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.getLanguageManager().getTitle(languageId, built)
        ICoreAPI.INSTANCE.getLanguageBridge().sendTitle(this@Player, built, component)
    }

    override fun openBook(provider: BookComponentProvider.() -> Unit): Deferred<Unit> {
        val built = BookComponentProvider().apply(provider)
        if (built.key.isNullOrBlank()) throw IllegalArgumentException("Key not set")
        val component = ICoreAPI.INSTANCE.getLanguageManager().get(languageId, built)
    }
    override fun getPlaceholders(prefix: String): PlaceholderCollection {
        if (prefix == "player") return placeholders
        return placeholders.copy(prefix)
    }

}