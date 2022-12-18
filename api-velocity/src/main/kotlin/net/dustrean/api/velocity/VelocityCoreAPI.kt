package net.dustrean.api.velocity

import com.velocitypowered.api.proxy.ProxyServer
import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.parser.string.StringParser
import net.dustrean.api.velocity.command.VelocityCommandManager
import net.dustrean.api.velocity.utils.parser.PlayerParser

object VelocityCoreAPI : CloudCoreAPI() {

    lateinit var proxyServer: ProxyServer
        private set

    override fun getCommandManager() = VelocityCommandManager

    fun init(proxyServer: ProxyServer) {
        this.proxyServer = proxyServer

        ExceptionHandler

        StringParser.customTypeParsers.add(PlayerParser())
    }
}