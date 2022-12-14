package net.dustrean.api

import net.dustrean.api.command.ICommandManager
import net.dustrean.api.event.IEventManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.IPacketManager

interface ICoreAPI {
    fun getNetworkComponentInfo(): NetworkComponentInfo

    fun getCoreVersion(): String

    fun getModuleHandler(): IModuleManager

    fun getPacketManager(): IPacketManager

    fun getEventManager(): IEventManager

    fun getCommandManager(): ICommandManager? = null
}