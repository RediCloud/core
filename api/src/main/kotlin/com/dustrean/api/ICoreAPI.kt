package com.dustrean.api

import com.dustrean.api.module.IModuleManager
import com.dustrean.api.network.NetworkComponentInfo
import com.dustrean.api.tasks.Scheduler

interface ICoreAPI {
    fun getNetworkComponentInfo(): NetworkComponentInfo

    fun getCoreVersion(): String

    fun getScheduler(): Scheduler<*, *>

    fun getModuleHandler(): IModuleManager
}