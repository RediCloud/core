package net.dustrean.api.player

import com.google.gson.annotations.Expose
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.data.ICacheValidator
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.LanguageManager
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.packet.connect.PlayerChangeServicePacket
import java.util.*

data class Player(
    override val uuid: UUID,
    override var name: String,
    override var coins: Long = 0,
    override var languageId: Int = LanguageManager.DEFAULT_LANGUAGE_ID,
    override var connected: Boolean = false,
) : IPlayer, ILanguagePlayer, AbstractDataObject(){
    class PlayerCacheHandler(val lastServer: () -> NetworkComponentInfo) : AbstractCacheHandler(),
        ICacheValidator<AbstractDataObject> {

        override fun isValid(): Boolean = lastServer() == ICoreAPI.INSTANCE.getNetworkComponentInfo()
        override suspend fun getCacheNetworkComponents(): Set<NetworkComponentInfo> = setOf(lastServer())
    }

    companion object {
        val INVALID_ID = UUID(0, 0)
        val INVALID_SERVICE = NetworkComponentInfo(NetworkComponentType.STANDALONE, INVALID_ID)
        const val INVALID_IP = "UNKNOWN"
    }

    override var lastServer: NetworkComponentInfo = INVALID_SERVICE
    override var lastProxy: NetworkComponentInfo = INVALID_SERVICE
    override var authentication: PlayerAuthentication = PlayerAuthentication()
    override val nameHistory: MutableList<Pair<Long, String>> = mutableListOf()
    override val sessions: MutableList<PlayerSession> = mutableListOf()
    @Expose(serialize = false, deserialize = false)
    private val placeholders = PlaceholderCollection()

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

    override suspend fun sendMessage(provider: ChatComponentBuilder.LanguageChatComponentProvider) {
        val component = ICoreAPI.INSTANCE.getLanguageManager().getChatMessage(languageId, provider)
        ICoreAPI.INSTANCE.getLanguageBridge().sendMessage(this, provider, component)
    }

    override fun getPlaceholders(prefix: String): PlaceholderCollection {
        if(prefix.isEmpty()) return placeholders
        return placeholders.copy(prefix)
    }

}