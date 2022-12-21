package net.dustrean.api

import net.dustrean.api.command.ICommandManager
import net.dustrean.api.config.IConfigManager
import net.dustrean.api.event.IEventManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.network.INetworkComponentManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.IPacketManager
import net.dustrean.api.player.IPlayerManager
import net.dustrean.api.redis.IRedisConnection

interface ICoreAPI {

    companion object {
        lateinit var INSTANCE: ICoreAPI

        fun <T : ICoreAPI> getInstance() : T {
            return INSTANCE as T
        }
    }

    fun getRedisConnection(): IRedisConnection

    fun getConfigManager(): IConfigManager

    fun getNetworkComponentInfo(): NetworkComponentInfo

    fun getCoreVersion(): String

    fun getModuleHandler(): IModuleManager

    fun getPacketManager(): IPacketManager

    fun getEventManager(): IEventManager

    fun getCommandManager(): ICommandManager

    fun getNetworkComponentManager(): INetworkComponentManager

    fun getPlayerManager(): IPlayerManager

    fun shutdown()
}