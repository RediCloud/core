package dev.redicloud.api.minestom.bootstrap

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.minestom.MinestomCoreAPI
import dev.redicloud.api.redis.codec.GsonCodec
import dev.redicloud.libloader.boot.Bootstrap
import dev.redicloud.libloader.boot.apply.impl.ClassLoaderResourceLoader
import net.minestom.server.extensions.Extension
import net.minestom.server.extensions.ExtensionClassLoader

class MinestomBootstrap : Extension() {
    lateinit var classloader: ExtensionClassLoader
    override fun preInitialize() {
        val classloader = this.javaClass.classLoader as ExtensionClassLoader
        this.classloader = classloader
        Bootstrap().apply({
            classloader.addURL(it)
        }, classloader, ClassLoaderResourceLoader("core-minestom_plugin", classloader))
    }

    override fun initialize() {
        MinestomCoreAPI.init()
        (ICoreAPI.getInstance<CoreAPI>().redisConnection.redisClient.config.codec as GsonCodec).classLoaders.add(
            classloader
        )
    }

    override fun terminate() {
        MinestomCoreAPI.shutdown()
    }
}
