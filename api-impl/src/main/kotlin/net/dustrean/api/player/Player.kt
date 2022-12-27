package net.dustrean.api.player

import com.google.gson.annotations.Expose
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.data.ICacheValidator
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.packet.connect.PlayerChangeServicePacket
import java.util.*

data class Player(
    override val uuid: UUID,
    override var name: String,
    override var languageID: Int = 0,
    override var coins: Long = 0,
    override var connected: Boolean = false,
) : IPlayer, AbstractDataObject() {
    companion object {
        val INVALID_ID = UUID(0, 0)
        val INVALID_SERVICE = NetworkComponentInfo(NetworkComponentType.STANDALONE, INVALID_ID)
        val INVALID_IP = "UNKNOWN"
    }

    override var lastServer: NetworkComponentInfo = INVALID_SERVICE
    override var lastProxy: NetworkComponentInfo = INVALID_SERVICE
    override var authentication: IPlayerAuthentication = PlayerAuthentication()
    override val nameHistory: MutableList<Pair<Long, String>> = mutableListOf()
    override val sessions: MutableList<IPlayerSession> = mutableListOf()
    @Expose(serialize = false, deserialize = false)
    private val cacheHandler = object : AbstractCacheHandler() {
        override suspend fun getCacheNetworkComponents(): Set<NetworkComponentInfo> =
            setOf(lastServer)
    }
    @Expose(serialize = false, deserialize = false)
    private val validator = object : ICacheValidator<AbstractDataObject> {
        override fun isValid(): Boolean {
            return lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
        }
    }

    override suspend fun update(): Player =
        ICoreAPI.getInstance<CoreAPI>().getPlayerManager().updatePlayer(this)

    override fun getCurrentSession(): PlayerSession? {
        val session = sessions.lastOrNull() ?: return null
        return if (session.isActive()) session as PlayerSession else null
    }

    override fun getLastSession(): PlayerSession? {
        val session = sessions.lastOrNull { !it.isActive() } ?: return null
        return session as PlayerSession
    }

    override fun getIdentifier(): UUID =
        uuid

    override fun getCacheHandler(): AbstractCacheHandler =
        cacheHandler

    override fun getValidator(): ICacheValidator<AbstractDataObject> =
        validator

    override fun isOnCurrent(): Boolean =
        when (ICoreAPI.INSTANCE.getNetworkComponentInfo().type) {
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

}