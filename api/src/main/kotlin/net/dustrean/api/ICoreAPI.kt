package net.dustrean.api

import net.dustrean.api.event.IEventManager
import net.dustrean.api.module.IModuleManager
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.IPacketManager
import net.dustrean.api.tasks.Scheduler

interface ICoreAPI {
    fun getNetworkComponentInfo(): NetworkComponentInfo

    fun getCoreVersion(): String

    fun getScheduler(): Scheduler<*, *>

    fun getModuleHandler(): IModuleManager

    fun getPacketManager(): IPacketManager

    fun getEventManager(): IEventManager
}