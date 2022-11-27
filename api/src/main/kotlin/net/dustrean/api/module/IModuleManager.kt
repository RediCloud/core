package net.dustrean.api.module

interface IModuleManager {

    fun getModule(name: String): Module?

    fun getModuleDescription(name: String): ModuleDescription?

    fun getModules(state: ModuleState): List<Module>

    fun loadModule(description: ModuleDescription): Boolean

    fun unloadModule(module: Module): Boolean

    fun unloadModule(name: String): Boolean

    fun enableModules()
    fun disableModules()

    fun reloadModules()
}