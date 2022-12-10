package net.dustrean.api.standalone.impl

import net.dustrean.api.CoreAPI
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import java.util.*

class StandaloneCoreAPI: CoreAPI(NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.randomUUID()))