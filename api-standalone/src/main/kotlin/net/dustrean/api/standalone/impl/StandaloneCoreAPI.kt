package net.dustrean.api.standalone.impl

import net.dustrean.api.CoreAPI
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.standalone.commands.StandaloneCommandManager
import net.dustrean.api.utils.ExceptionHandler
import java.util.*

object StandaloneCoreAPI : CoreAPI(NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.randomUUID())) {

    init {
        ExceptionHandler.service = "StandaloneCoreAPI"
    }

    override fun getCommandManager() = StandaloneCommandManager

}