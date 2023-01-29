package net.dustrean.api

import net.dustrean.api.config.ConfigManager
import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.event.EventManager
import net.dustrean.api.event.IEventManager
import net.dustrean.api.language.ILanguageManager
import net.dustrean.api.language.LanguageManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.module.ModuleManager
import net.dustrean.api.network.INetworkComponentManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentManager
import net.dustrean.api.packet.IPacketManager
import net.dustrean.api.packet.PacketManager
import net.dustrean.api.player.PlayerManager
import net.dustrean.api.redis.RedisConnection
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.coreVersion

abstract class CoreAPI(
    final override val networkComponentInfo: NetworkComponentInfo
) : ICoreAPI {

    init {
        ICoreAPI.INSTANCE = this
        ExceptionHandler
    }

    final override val redisConnection: RedisConnection = RedisConnection()
    override val packetManager: PacketManager = PacketManager(networkComponentInfo, redisConnection)
    override val eventManager: EventManager = EventManager()
    override val networkComponentManager: NetworkComponentManager = NetworkComponentManager(redisConnection).also {
        it.networkComponents[networkComponentInfo.getKey()] = networkComponentInfo
    }
    override val configManager: ConfigManager = ConfigManager(redisConnection)
    override val playerManager: PlayerManager = PlayerManager(this)
    override val languageManager: LanguageManager = LanguageManager(this)
    override val moduleManager: ModuleManager = ModuleManager(this).also { it.enableModules() }

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

}