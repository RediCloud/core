package net.dustrean.api.minestom.bootstrap

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.minestom.MinestomCoreAPI
import net.dustrean.api.redis.codec.GsonCodec
import net.dustrean.libloader.boot.Bootstrap
import net.minestom.server.extensions.DiscoveredExtension
import net.minestom.server.extensions.Extension
import net.minestom.server.extensions.ExtensionClassLoader
import java.net.URL

class MinestomBootstrap : Extension() {
    lateinit var classloader: ExtensionClassLoader
    override fun preInitialize() {
        val classloader = this.javaClass.superclass.classLoader as ExtensionClassLoader
        this.classloader = classloader
        Bootstrap().apply({
            classloader.addURL(it)
        }, classloader, classloader)
    }

    override fun initialize() {
        MinestomCoreAPI.init()
        (ICoreAPI.getInstance<CoreAPI>()
            .getRedisConnection().redisClient.config.codec as GsonCodec).classLoaders.add(
            this.javaClass.superclass.getDeclaredMethod(
                "getExtensionClassLoader"
            ).apply { isAccessible = true }.invoke(this) as ExtensionClassLoader
        )
    }

    override fun terminate() {
        MinestomCoreAPI.shutdown()
    }
}
