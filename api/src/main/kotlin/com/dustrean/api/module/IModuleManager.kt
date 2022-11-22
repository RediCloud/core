package com.dustrean.api.module

interface IModuleManager {
    fun getModules(): List<IModule>

    fun getModule(name: String): IModule?

    fun getModuleDescription(name: String): IModuleDescription?

    fun getModules(state: ModuleState) : List<IModule>

    fun canLoadModule(description: IModuleDescription): Boolean

    fun loadModule(description: IModuleDescription): Boolean

    fun unloadModule(name: String): Boolean
}