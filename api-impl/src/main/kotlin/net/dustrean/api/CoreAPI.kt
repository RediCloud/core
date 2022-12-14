package net.dustrean.api

import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.event.EventManager
import net.dustrean.api.event.IEventManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.module.ModuleManager
import net.dustrean.api.network.INetworkComponentManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentManager
import net.dustrean.api.packet.IPacketManager
import net.dustrean.api.packet.PacketManager
import net.dustrean.api.redis.RedisConnection
import net.dustrean.api.utils.coreVersion

abstract class CoreAPI(
    private val networkComponentInfo: NetworkComponentInfo
) : ICoreAPI {

    private var redisConnection: RedisConnection = RedisConnection()
    private var packetManager: PacketManager = PacketManager(networkComponentInfo, redisConnection)
    private var eventManager: EventManager = EventManager()
    private var networkComponentManager: NetworkComponentManager = NetworkComponentManager(redisConnection)
    private var moduleManager: ModuleManager = ModuleManager(this)

    init {
        ICoreAPI.INSTANCE = this
        networkComponentManager.networkComponents[networkComponentInfo.getKey()] = networkComponentInfo

        moduleManager.enableModules()
    }

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

    override fun getRedisConnection(): RedisConnection = redisConnection
    override fun getNetworkComponentInfo(): NetworkComponentInfo = networkComponentInfo

    override fun getPacketManager(): IPacketManager = packetManager

    override fun getCoreVersion(): String = coreVersion

    override fun getEventManager(): IEventManager = eventManager

    override fun getModuleHandler(): IModuleManager = moduleManager

    override fun getNetworkComponentManager(): INetworkComponentManager = networkComponentManager

}