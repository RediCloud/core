package net.dustrean.api.velocity.boostrap

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.dustrean.api.velocity.VelocityCoreAPI

@Plugin(
    id = "core",
    name = "core",
    version = "1.0.0-SNAPSHOT",
    description = "Core plugin for the Dustrean API",
    authors = ["Dustrean-Team"]
)
class VelocityBootstrap(proxyServer: ProxyServer) {

    init {
        VelocityCoreAPI.init(proxyServer)
        proxyServer.eventManager.register(this, this)
    }

    @Subscribe
    fun onShutdown(event: com.velocitypowered.api.event.proxy.ProxyShutdownEvent) {
        VelocityCoreAPI.shutdown()
    }
}

