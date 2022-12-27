package net.dustrean.api.minestom.bootstrap

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.minestom.MinestomCoreAPI
import net.dustrean.api.redis.codec.GsonCodec
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
