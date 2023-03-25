package dev.redicloud.api.minestom.bootstrap

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.minestom.MinestomCoreAPI
import dev.redicloud.api.redis.codec.GsonCodec
import dev.redicloud.libloader.boot.Bootstrap
import dev.redicloud.libloader.boot.apply.impl.JarResourceLoader
import net.minestom.server.extensions.Extension
import net.minestom.server.extensions.ExtensionClassLoader

class MinestomBootstrap : Extension() {
    lateinit var classLoader: ExtensionClassLoader
    override fun preInitialize() {
        classLoader = this.javaClass.classLoader as ExtensionClassLoader
        Bootstrap().apply({
            classLoader.addURL(it)
        }, classLoader, JarResourceLoader("core-minestom_api", origin.originalJar))
    }

    override fun initialize() {
        MinestomCoreAPI.init()
        (ICoreAPI.getInstance<CoreAPI>().redisConnection.redisClient.config.codec as GsonCodec).classLoaders.add(
            classLoader
        )
    }

    override fun terminate() {
        MinestomCoreAPI.shutdown()
    }
}
