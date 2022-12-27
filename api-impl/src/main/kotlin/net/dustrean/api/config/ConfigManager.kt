package net.dustrean.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.dustrean.api.ICoreAPI
import net.dustrean.api.packet.PacketManager
import net.dustrean.api.redis.RedisConnection

class ConfigManager(private val redisConnection: RedisConnection) : IConfigManager {

    private val cache = mutableListOf<IConfig>()
    private val json = Json
    private val objectMapper = ObjectMapper()

    init {
        PacketManager.INSTANCE.registerPacket(ConfigUpdatePacket())
    }

    override suspend fun <T : IConfig> getConfig(key: String): T {
        if (cache.any { it.key == key }) {
            return cache.first { it.key == key } as T
        }
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        if (!bucket.isExists) throw NoSuchElementException("Config with key $key does not exists")
        val configData = bucket.get()
        val config = json.decodeFromString(serializer(Class.forName(configData.clazz)), configData.json)
        cache.add(config as IConfig)
        return config as T
    }

    override suspend fun <T : IConfig> createConfig(config: T): T {
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:${config.key}")
        if (bucket.isExists) throw IllegalArgumentException("Config with key ${config.key} already exists")
        val configData = ConfigData(json.encodeToString(serializer(config.javaClass), config), config.javaClass.name)
        bucket.set(configData)
        cache.add(config)
        return config
    }

    override suspend fun deleteConfig(key: String) {
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        if (!bucket.isExists) throw NoSuchElementException("Config with key $key does not exist")
        bucket.delete()
        cache.removeIf { it.key == key }
        val packet = ConfigUpdatePacket()
        packet.key = key
        packet.delete = true
        packet.configData = ConfigData("", "")
        ICoreAPI.INSTANCE.getNetworkComponentManager().getComponentInfos().forEach {
            if (it == ICoreAPI.INSTANCE.getNetworkComponentInfo())
                packet.packetData.addReceiver(it)
        }
        packet.sendPacket()
    }

    override suspend fun deleteConfig(config: IConfig) = deleteConfig(config.key)

    override suspend fun <T : IConfig> saveConfig(config: T) {
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:${config.key}")
        if (!bucket.isExists) throw NoSuchElementException("Config with key ${config.key} does not exist")
        val configData = ConfigData(json.encodeToString(serializer(config.javaClass), config), config.javaClass.name)
        bucket.set(configData)
        cache.add(config)
        val packet = ConfigUpdatePacket()
        packet.key = config.key
        packet.configData = configData
        ICoreAPI.INSTANCE.getNetworkComponentManager().getComponentInfos().forEach { componentInfo ->
            if (componentInfo == ICoreAPI.INSTANCE.getNetworkComponentInfo()) return@forEach
            packet.packetData.addReceiver(componentInfo)
        }
        packet.sendPacket()
    }

    override suspend fun exists(config: IConfig): Boolean {
        return exists(config.key)
    }

    override suspend fun exists(key: String): Boolean {
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        return bucket.isExists
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
        val config = json.decodeFromString(serializer(clazz), configData.json)
        cache.add(config as IConfig)
    }

}