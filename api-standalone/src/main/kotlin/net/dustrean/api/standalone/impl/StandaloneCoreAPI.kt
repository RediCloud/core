package net.dustrean.api.standalone.impl

import net.dustrean.api.CoreAPI
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.standalone.commands.StandaloneCommandManager
import net.dustrean.api.standalone.language.LanguageBridge
import net.dustrean.api.utils.ExceptionHandler
import java.util.*

object StandaloneCoreAPI : CoreAPI(NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.randomUUID())) {

    private val languageBridge = LanguageBridge()

    override fun getCommandManager() = StandaloneCommandManager

    override fun getLanguageBridge(): LanguageBridge = languageBridge

}