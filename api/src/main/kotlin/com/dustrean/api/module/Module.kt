package com.dustrean.api.module

import com.dustrean.api.CoreAPI
import java.net.URLClassLoader

abstract class Module{
    var state = ModuleState.DISABLED
    lateinit var description: ModuleDescription
    lateinit var classLoader: URLClassLoader

    abstract fun onLoad(api: CoreAPI)
    abstract fun onEnable(api: CoreAPI)
    abstract fun onDisable(api: CoreAPI)
}