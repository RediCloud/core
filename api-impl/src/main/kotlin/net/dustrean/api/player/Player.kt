package net.dustrean.api.player

import kotlinx.coroutines.Deferred
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.data.ICacheValidator
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

data class Player(
    override val uuid: UUID,
    override var name: String,
    override var languageID: Int = 0,
    override var coins: Long = 0,
    override var currentlyOnline: Boolean = false
) : IPlayer, AbstractDataObject() {
    companion object {
        val INVALID = NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID(0, 0))
    }
    override var lastServer: NetworkComponentInfo = INVALID
    override var lastProxy: NetworkComponentInfo = INVALID
    override val nameHistory: MutableList<Pair<Long, String>> = mutableListOf()

    private val cacheHandler = object: AbstractCacheHandler<AbstractDataObject>() {
        override fun getCacheNetworkComponents(): FutureAction<List<NetworkComponentInfo>> =
            FutureAction(listOf(lastServer))
    }

    private val validator = object: ICacheValidator<AbstractDataObject> {
        override fun isValid(): Boolean {
            return lastServer == ICoreAPI.INSTANCE.getNetworkComponentInfo()
        }
    }
    override fun update(): FutureAction<Player> =
        ICoreAPI.getInstance<CoreAPI>().getPlayerManager().updatePlayer(this)


    override fun getIdentifier(): UUID =
        uuid

    override fun getCacheHandler(): AbstractCacheHandler<AbstractDataObject> =
        cacheHandler

    override fun getValidator(): ICacheValidator<AbstractDataObject>? = validator // TODO()


}