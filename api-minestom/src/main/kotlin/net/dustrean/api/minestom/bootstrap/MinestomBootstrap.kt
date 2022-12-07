package net.dustrean.api.minestom.bootstrap

import net.dustrean.api.minestom.MinestomCoreAPI
import net.minestom.server.extensions.Extension

class MinestomBootstrap: Extension() {

    override fun initialize() {
        MinestomCoreAPI.init()
    }

    override fun terminate() {
        MinestomCoreAPI.shutdown()
    }
}
