package net.dustrean.api.module

import net.dustrean.api.ICoreAPI

abstract class Module {
    var state = ModuleState.DISABLED

    // The module description will be set by the module manager
    val description: ModuleDescription = ModuleDescription("unknown", "unknown", "unknown", HashMap())

    abstract fun onLoad(api: ICoreAPI)
    abstract fun onEnable(api: ICoreAPI)
    abstract fun onDisable(api: ICoreAPI)
}