package datamanager

import net.dustrean.api.CoreAPI
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import java.util.*

class TestCoreAPI(
    private var serverId: UUID
) : CoreAPI(NetworkComponentInfo(NetworkComponentType.STANDALONE, serverId)){

    val playerManager = TestPlayerManager(redisConnection)

}