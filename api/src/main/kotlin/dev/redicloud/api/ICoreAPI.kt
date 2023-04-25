package dev.redicloud.api

import dev.redicloud.api.command.ICommandManager
import dev.redicloud.api.config.IConfigManager
import dev.redicloud.api.event.IEventManager
import dev.redicloud.api.event.impl.CoreInitializedEvent
import dev.redicloud.api.language.ILanguageBridge
import dev.redicloud.api.language.ILanguageManager
import dev.redicloud.api.module.IModuleManager
import dev.redicloud.api.network.INetworkComponentManager
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.packet.IPacketManager
import dev.redicloud.api.player.IPlayerManager
import dev.redicloud.api.redis.IRedisConnection

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

    fun <T: ICoreAPI> asCore(): T = this as T

    fun initialized() {
        this.eventManager.callEvent(CoreInitializedEvent(this))
    }

}