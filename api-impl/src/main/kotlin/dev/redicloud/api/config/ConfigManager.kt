package dev.redicloud.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.packet.PacketManager
import dev.redicloud.api.redis.RedisConnection
import dev.redicloud.api.utils.extension.JsonObjectData

class ConfigManager(private val redisConnection: RedisConnection) : IConfigManager {

    private val cache = mutableListOf<IConfig>()
    private val gson = Gson()
    private val objectMapper = ObjectMapper()

    init {
        PacketManager.INSTANCE.registerPacket(ConfigUpdatePacket())
    }

    override suspend fun <T : IConfig> getConfig(key: String, clazz: Class<T>): T {
        if (cache.any { it.key == key }) {
            return cache.first { it.key == key } as T
        }
        val bucket = redisConnection.redisClient.getBucket<JsonObjectData>("config:$key")
        if (!bucket.isExists) throw NoSuchElementException("Config with key $key does not exists")
        val configData = bucket.get()
        val config = gson.fromJson(configData.json, clazz)
        cache.add(config as IConfig)
        return config as T
    }

    override suspend fun <T : IConfig> getConfigOrPut(key: String, clazz: Class<T>, default: () -> T): T {
        return try {
            getConfig(key, clazz)
        } catch(e: NoSuchElementException) {
            val conf = default()
            GlobalScope.launch {
                createConfig(conf)
            }
            conf
        }
    }

    override suspend fun <T : IConfig> createConfig(config: T): T {
        val bucket = redisConnection.redisClient.getBucket<JsonObjectData>("config:${config.key}")
        if (bucket.isExists) throw IllegalArgumentException("Config with key ${config.key} already exists")
        val configData = JsonObjectData(gson.toJson(config), config.javaClass.name)
        bucket.set(configData)
        cache.add(config)
        return config
    }

    override suspend fun deleteConfig(key: String) {
        val bucket = redisConnection.redisClient.getBucket<JsonObjectData>("config:$key")
        if (!bucket.isExists) throw NoSuchElementException("Config with key $key does not exist")
        bucket.delete()
        cache.removeIf { it.key == key }
        val packet = ConfigUpdatePacket()
        packet.key = key
        packet.delete = true
        packet.configData = JsonObjectData("", "")
        ICoreAPI.INSTANCE.networkComponentManager.getComponentInfos().forEach {
            if (it == ICoreAPI.INSTANCE.networkComponentInfo)
                packet.packetData.addReceiver(it)
        }
        packet.sendPacket()
    }

    override suspend fun deleteConfig(config: IConfig) = deleteConfig(config.key)

    override suspend fun <T : IConfig> saveConfig(config: T) {
        val bucket = redisConnection.redisClient.getBucket<JsonObjectData>("config:${config.key}")
        if (!bucket.isExists) throw NoSuchElementException("Config with key ${config.key} does not exist")
        val configData = JsonObjectData(gson.toJson(config), config.javaClass.name)
        bucket.set(configData)
        cache.add(config)
        val packet = ConfigUpdatePacket()
        packet.key = config.key
        packet.configData = configData
        ICoreAPI.INSTANCE.networkComponentManager.getComponentInfos().forEach { componentInfo ->
            if (componentInfo.equals(ICoreAPI.INSTANCE.networkComponentInfo)) return@forEach
            packet.packetData.addReceiver(componentInfo)
        }
        packet.sendPacket()
    }

    override suspend fun exists(config: IConfig): Boolean {
        return exists(config.key)
    }

    override suspend fun exists(key: String): Boolean {
        val bucket = redisConnection.redisClient.getBucket<JsonObjectData>("config:$key")
        return bucket.isExists
    }

    fun readUpdate(key: String, configData: JsonObjectData, delete: Boolean) {
        if (delete) {
            cache.removeIf { it.key == key }
            return
        }
        if (cache.any { it.key == key }) {
            val config = cache.first { it.key == key }
            objectMapper.readerForUpdating(config).readValue<IConfig>(configData.json)
            return
        }
    }

}