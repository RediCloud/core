package net.dustrean.api

import net.dustrean.api.event.IEventManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.IPacketManager

interface ICoreAPI {

    companion object {
        lateinit var INSTANCE: ICoreAPI

        fun <T : ICoreAPI> getInstance() : T {
            return INSTANCE as T
        }
    }

    fun getNetworkComponentInfo(): NetworkComponentInfo

    fun getCoreVersion(): String

    fun getModuleHandler(): IModuleManager

    fun getPacketManager(): IPacketManager

    fun getEventManager(): IEventManager

    fun shutdown()
}