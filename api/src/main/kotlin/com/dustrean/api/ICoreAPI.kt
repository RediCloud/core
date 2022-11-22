package com.dustrean.api

import com.dustrean.api.module.IModuleManager
import com.dustrean.api.tasks.Scheduler

interface ICoreAPI {

    fun getCoreVersion(): String

    fun getScheduler(): Scheduler<*, *>

    fun getModuleHandler(): IModuleManager

}