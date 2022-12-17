package net.dustrean.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import net.dustrean.api.ICoreAPI
import net.dustrean.api.packet.PacketManager
import net.dustrean.api.redis.RedisConnection
import net.dustrean.api.tasks.futures.FutureAction

class ConfigManager(private val redisConnection: RedisConnection) : IConfigManager {

    private val cache = mutableListOf<IConfig>()
    private val gson = Gson()
    private val objectMapper = ObjectMapper()

    init {
        PacketManager.INSTANCE.registerPacket(ConfigUpdatePacket())
    }

    override fun <T : IConfig> getConfig(key: String): FutureAction<T> {
        val future = FutureAction<T>()
        if (cache.any { it.key == key }) {
            future.complete(cache.first { it.key == key } as T)
            return future
        }
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if (!exists) {
                future.completeExceptionally(NoSuchElementException("Config with key $key does not exist"))
                return@whenComplete
            }
            bucket.async.whenComplete { configData, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                try {
                    val config = gson.fromJson(configData.json, Class.forName(configData.clazz))
                    cache.add(config as IConfig)
                    future.complete(config as T)
                } catch (e: Exception) {
                    future.completeExceptionally(e)
                }
            }
        }

        return future
    }

    override fun <T : IConfig> createConfig(config: T): FutureAction<T> {
        val future = FutureAction<T>()
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:${config.key}")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if (exists) {
                future.completeExceptionally(IllegalArgumentException("Config with key ${config.key} already exists"))
                return@whenComplete
            }
            val configData = ConfigData(gson.toJson(config), config.javaClass.name)
            bucket.setAsync(configData).whenComplete { _, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                cache.add(config)
                future.complete(config)
            }
        }
        return future
    }

    override fun deleteConfig(key: String): FutureAction<Unit> {
        val future = FutureAction<Unit>()
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if (!exists) {
                future.completeExceptionally(NoSuchElementException("Config with key $key does not exist"))
                return@whenComplete
            }
            bucket.deleteAsync().whenComplete { _, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                cache.removeIf { it.key == key }
                val packet = ConfigUpdatePacket()
                packet.key = key
                packet.delete = true
                packet.configData = ConfigData("", "")
                ICoreAPI.INSTANCE.getNetworkComponentManager().getComponentInfos()
                    .whenComplete { componentInfos, throwable2 ->
                        if (throwable2 != null) {
                            future.completeExceptionally(throwable2)
                            return@whenComplete
                        }
                        componentInfos.forEach { componentInfo ->
                            if (componentInfo.equals(ICoreAPI.INSTANCE.getNetworkComponentInfo())) return@forEach
                            packet.packetData.addReceiver(componentInfo)
                        }
                        PacketManager.INSTANCE.sendPacketAsync(packet)
                        future.complete(Unit)
                    }
            }
        }
        return future
    }

    override fun deleteConfig(config: IConfig): FutureAction<Unit> = deleteConfig(config.key)

    override fun <T : IConfig> saveConfig(config: T): FutureAction<Unit> {
        val future = FutureAction<Unit>()
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:${config.key}")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if (!exists) {
                future.completeExceptionally(NoSuchElementException("Config with key ${config.key} does not exist"))
                return@whenComplete
            }
            val configData = ConfigData(gson.toJson(config), config.javaClass.name)
            bucket.setAsync(configData).whenComplete { _, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                cache.add(config)
                val packet = ConfigUpdatePacket()
                packet.key = config.key
                packet.configData = configData
                ICoreAPI.INSTANCE.getNetworkComponentManager().getComponentInfos()
                    .whenComplete { componentInfos, throwable2 ->
                        if (throwable2 != null) {
                            future.completeExceptionally(throwable2)
                            return@whenComplete
                        }
                        componentInfos.forEach { componentInfo ->
                            if (componentInfo.equals(ICoreAPI.INSTANCE.getNetworkComponentInfo())) return@forEach
                            packet.packetData.addReceiver(componentInfo)
                        }
                        PacketManager.INSTANCE.sendPacketAsync(packet)
                        future.complete(Unit)
                    }
            }
        }
        return future
    }

    fun readUpdate(key: String, configData: ConfigData, delete: Boolean) {
        if (delete) {
            cache.removeIf { it.key == key }
            return
        }
        val clazzName = configData.clazz
        val clazz = Class.forName(clazzName)
        if (cache.any { it.key == key }) {
            val config = cache.first { it.key == key }
            objectMapper.readerForUpdating(config).readValue<IConfig>(configData.json)
            return
        }
        val config = gson.fromJson(configData.json, clazz)
        cache.add(config as IConfig)
    }

}