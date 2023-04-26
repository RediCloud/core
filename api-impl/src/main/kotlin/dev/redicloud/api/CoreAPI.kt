package dev.redicloud.api

import dev.redicloud.api.config.ConfigManager
import dev.redicloud.api.data.AbstractDataManager
import dev.redicloud.api.event.EventManager
import dev.redicloud.api.event.impl.CoreInitializedEvent
import dev.redicloud.api.language.LanguageManager
import dev.redicloud.api.module.ModuleManager
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.network.NetworkComponentManager
import dev.redicloud.api.packet.PacketManager
import dev.redicloud.api.player.PlayerManager
import dev.redicloud.api.redis.RedisConnection
import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CoreAPI(
    final override val networkComponentInfo: NetworkComponentInfo
) : ICoreAPI {

    init {
        ICoreAPI.INSTANCE = this
    }

    final override val logger: Logger = LoggerFactory.getLogger(ModuleManager::class.java)
    final override val redisConnection: RedisConnection = RedisConnection()
    override val packetManager: PacketManager = PacketManager(networkComponentInfo, redisConnection)
    override val eventManager: EventManager = EventManager()
    override val networkComponentManager: NetworkComponentManager = NetworkComponentManager(redisConnection).also {
        it.networkComponents[networkComponentInfo.getKey()] = networkComponentInfo
    }
    override val configManager: ConfigManager = ConfigManager(redisConnection)
    override val playerManager: PlayerManager = PlayerManager(this)
    override val languageManager: LanguageManager = LanguageManager(this)
    override val moduleManager: ModuleManager = ModuleManager(this)

    override fun shutdown() {
        moduleManager.disableModules()
        AbstractDataManager.MANAGERS.forEach { (_, manager) ->
            manager.unregisterCache()
        }
        networkComponentManager.networkComponents.remove(networkComponentInfo.getKey())
        if (redisConnection.isConnected()) {
            redisConnection.disconnect()
        }
    }

    fun initialized() {
        this.logger.info("Core initialized")
        this.eventManager.callEvent(CoreInitializedEvent(this))
    }

}