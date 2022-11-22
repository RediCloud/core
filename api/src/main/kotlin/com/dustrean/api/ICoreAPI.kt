package com.dustrean.api

import com.dustrean.api.module.IModuleManager
import com.dustrean.api.network.INetworkComponentInfo
import com.dustrean.api.tasks.Scheduler

interface ICoreAPI {

    fun getNetworkComponentInfo(): INetworkComponentInfo

    fun getCoreVersion(): String

    fun getScheduler(): Scheduler<*, *>

    fun getModuleHandler(): IModuleManager

}