package net.dustrean.api.standalone.impl

import net.dustrean.api.CoreAPI
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.standalone.commands.StandaloneCommandManager
import java.util.*

object StandaloneCoreAPI : CoreAPI(NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.randomUUID())) {

    override fun getCommandManager() = StandaloneCommandManager

}