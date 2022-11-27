package net.dustrean.api.module

import net.dustrean.api.ICoreAPI
import java.net.URLClassLoader

abstract class Module{
    var state = ModuleState.DISABLED
    lateinit var description: ModuleDescription
    lateinit var classLoader: URLClassLoader

    abstract fun onLoad(api: ICoreAPI)
    abstract fun onEnable(api: ICoreAPI)
    abstract fun onDisable(api: ICoreAPI)
}