package net.dustrean.api.velocity

import com.velocitypowered.api.proxy.ProxyServer
import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.parser.string.StringParser
import net.dustrean.api.velocity.command.VelocityCommandManager
import net.dustrean.api.velocity.event.PlayerEvents
import net.dustrean.api.velocity.language.VelocityLanguageBridge
import net.dustrean.api.velocity.utils.parser.PlayerParser

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