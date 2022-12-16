package net.dustrean.api.module

import net.dustrean.api.ICoreAPI
import net.dustrean.api.network.NetworkComponentType

abstract class Module {
    var state = ModuleState.DISABLED

    // The module description will be set by the module manager
    val description: ModuleDescription = ModuleDescription("unknown", "unknown", "unknown", emptyMap())

    abstract fun onLoad(api: ICoreAPI)
    abstract fun onEnable(api: ICoreAPI)
    abstract fun onDisable(api: ICoreAPI)
}