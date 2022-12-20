package net.dustrean.api.player

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.data.ICacheValidator
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
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
        val INVALID_AUTHENTICATION = PlayerAuthentication()
    }
    override var lastServer: NetworkComponentInfo = INVALID_SERVICE
    override var lastProxy: NetworkComponentInfo = INVALID_SERVICE
    override var authentication: IPlayerAuthentication = INVALID_AUTHENTICATION
    override val nameHistory: MutableList<Pair<Long, String>> = mutableListOf()
    override val sessions: MutableList<Pair<Long, IPlayerSession>> = mutableListOf()
    private val cacheHandler = object: AbstractCacheHandler() {
        override suspend fun getCacheNetworkComponents(): Set<NetworkComponentInfo> =
            setOf(lastServer)
    }
    private val validator = object: ICacheValidator<AbstractDataObject> {
        override fun isValid(): Boolean {
            return lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
        }
    }

    override suspend fun update(): Player =
        ICoreAPI.getInstance<CoreAPI>().getPlayerManager().updatePlayer(this)

    override fun getCurrentSession(): PlayerSession? {
        val session = sessions.lastOrNull()?.second ?: return null
        return if(session.isActive()) session as PlayerSession else null
    }

    override fun getLastSession(): PlayerSession? =
        sessions.lastOrNull()?.second as PlayerSession ?: null

    override fun getIdentifier(): UUID =
        uuid

    override fun getCacheHandler(): AbstractCacheHandler =
        cacheHandler

    override fun getValidator(): ICacheValidator<AbstractDataObject> =
        validator

    override fun isOnline(): Boolean =
        when(ICoreAPI.INSTANCE.getNetworkComponentInfo().type) {
            NetworkComponentType.STANDALONE -> connected
            NetworkComponentType.VELOCITY -> lastProxy == ICoreAPI.INSTANCE.getNetworkComponentInfo()
            NetworkComponentType.MINESTOM -> lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
            NetworkComponentType.PAPER -> lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
    }

}