package datamanager

import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

class TestCacheHandler : AbstractCacheHandler<TestPlayer>() {

    override fun getCacheNetworkComponents(): FutureAction<List<NetworkComponentInfo>> {
        return FutureAction(listOf(NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.fromString("b04e8b0d-d274-4069-b7a6-fb26d7f17357"))))
    }

}