package net.dustrean.api.minestom.bootstrap

import net.dustrean.api.minestom.MinestomCoreAPI
import net.dustrean.libloader.boot.Bootstrap
import net.minestom.server.extensions.Extension
import net.minestom.server.extensions.ExtensionClassLoader

class MinestomBootstrap : Extension() {
    override fun preInitialize() {
        val classloader = this.javaClass.superclass.getDeclaredMethod(
            "getExtensionClassLoader"
        ).apply { isAccessible = true }.invoke(this) as ExtensionClassLoader
        Bootstrap().apply({
            classloader.addURL(it)
        }, classloader, classloader)
    }

    override fun initialize() {
        MinestomCoreAPI.init()
    }

    override fun terminate() {
        MinestomCoreAPI.shutdown()
    }
}
