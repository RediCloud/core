package dev.redicloud.api.standalone.impl

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.command.ICommandManager
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.network.NetworkComponentType
import dev.redicloud.api.standalone.commands.StandaloneCommandManager
import dev.redicloud.api.standalone.language.LanguageBridge
import java.util.*

object StandaloneCoreAPI : CoreAPI(NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.randomUUID())) {

    override val languageBridge = LanguageBridge()
    override val commandManager: ICommandManager = StandaloneCommandManager

}