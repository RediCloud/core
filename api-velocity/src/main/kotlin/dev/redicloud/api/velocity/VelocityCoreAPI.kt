package dev.redicloud.api.velocity

import com.velocitypowered.api.proxy.ProxyServer
import dev.redicloud.api.cloud.CloudCoreAPI
import dev.redicloud.api.utils.ExceptionHandler
import dev.redicloud.api.utils.parser.string.StringParser
import dev.redicloud.api.velocity.command.VelocityCommandManager
import dev.redicloud.api.velocity.event.PlayerEvents
import dev.redicloud.api.velocity.language.VelocityLanguageBridge
import dev.redicloud.api.velocity.utils.parser.PlayerParser

object VelocityCoreAPI : CloudCoreAPI() {

    override val languageBridge = VelocityLanguageBridge()
    override val commandManager = VelocityCommandManager
    lateinit var proxyServer: ProxyServer
        private set

    lateinit var plugin: Any


    fun init(proxyServer: ProxyServer, plugin: Any) {
        this.proxyServer = proxyServer
        this.plugin = plugin

        ExceptionHandler

        StringParser.customTypeParsers.add(PlayerParser())
        proxyServer.eventManager.register(plugin, PlayerEvents(playerManager))

    }
}