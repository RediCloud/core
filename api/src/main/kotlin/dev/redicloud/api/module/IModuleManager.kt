package dev.redicloud.api.module

import java.io.File

interface IModuleManager {

    fun getModule(name: String): Module?

    fun getModuleDescription(name: String): ModuleDescription?

    fun getModules(state: ModuleState): List<Module>

    fun loadModule(description: ModuleDescription, file: File): Boolean

    fun unloadModule(module: Module): Boolean

    fun unloadModule(name: String): Boolean

    fun enableModules()
    fun disableModules()

    fun reloadModules()

    fun getModuleLoaders(): List<ModuleClassLoader>
}