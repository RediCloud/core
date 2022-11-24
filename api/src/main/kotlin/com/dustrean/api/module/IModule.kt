package com.dustrean.api.module

import com.dustrean.api.CoreAPI

interface IModule{
    fun onLoad(api: CoreAPI)
    fun onEnable(api: CoreAPI)
    fun onDisable(api: CoreAPI)
}