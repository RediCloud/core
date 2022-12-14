package net.dustrean.api.velocity

import com.velocitypowered.api.proxy.ProxyServer
import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.parser.string.StringParser
import net.dustrean.api.velocity.command.VelocityCommandManager
import net.dustrean.api.velocity.utils.parser.PlayerParser

object VelocityCoreAPI : CloudCoreAPI() {

    // Instance will be set by reflections
    val proxyServer: ProxyServer = null!!
    val commandManager = VelocityCommandManager

    fun init(proxyServer: ProxyServer) {
        VelocityCoreAPI::class.java.getDeclaredField("proxyServer").apply {
            isAccessible = true
            set(this@VelocityCoreAPI, proxyServer)
            isAccessible = false
        }

        ExceptionHandler

        StringParser.customTypeParsers.add(PlayerParser())
    }
}