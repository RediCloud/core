package net.dustrean.api.module

import net.dustrean.api.CoreAPI

abstract class Module {
    var state = ModuleState.DISABLED

    // The module description will be set by the module manager
    val description: ModuleDescription = null!!

    abstract fun onLoad(api: CoreAPI)
    abstract fun onEnable(api: CoreAPI)
    abstract fun onDisable(api: CoreAPI)
}