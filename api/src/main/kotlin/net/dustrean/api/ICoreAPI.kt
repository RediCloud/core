package net.dustrean.api

import net.dustrean.api.command.ICommandManager
import net.dustrean.api.config.IConfigManager
import net.dustrean.api.event.IEventManager
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguageManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.network.INetworkComponentManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.IPacketManager
import net.dustrean.api.player.IPlayerManager
import net.dustrean.api.redis.IRedisConnection

interface ICoreAPI {

    companion object {
        lateinit var INSTANCE: ICoreAPI

        fun <T : ICoreAPI> getInstance(): T {
            return INSTANCE as T
        }
    }

    val redisConnection: IRedisConnection
    val configManager: IConfigManager
    val networkComponentInfo: NetworkComponentInfo
    val moduleManager: IModuleManager
    val packetManager: IPacketManager
    val eventManager: IEventManager
    val commandManager: ICommandManager
    val networkComponentManager: INetworkComponentManager
    val playerManager: IPlayerManager
    val languageManager: ILanguageManager
    val languageBridge: ILanguageBridge

    fun shutdown()
}