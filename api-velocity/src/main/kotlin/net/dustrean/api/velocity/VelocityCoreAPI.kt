package net.dustrean.api.velocity

import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.parser.string.StringParser
import net.dustrean.api.velocity.command.VelocityCommandManager
import net.dustrean.api.velocity.event.PlayerEvents
import net.dustrean.api.velocity.utils.parser.PlayerParser

object VelocityCoreAPI : CloudCoreAPI() {

    lateinit var proxyServer: ProxyServer
        private set

    lateinit var plugin: Plugin

    override fun getCommandManager() = VelocityCommandManager

    fun init(proxyServer: ProxyServer, plugin: Plugin) {
        this.proxyServer = proxyServer
        this.plugin = plugin

        ExceptionHandler

        StringParser.customTypeParsers.add(PlayerParser())
        proxyServer.eventManager.register(this, PlayerEvents(getPlayerManager()))
    }
}