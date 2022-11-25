package net.dustrean.api.module

import net.dustrean.api.CoreAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.jar.JarFile

class ModuleManager(
    var api: CoreAPI
) : IModuleManager {

    private val modules: MutableList<Module> = mutableListOf<Module>()

    fun detectModules(folder: File) {
        folder.listFiles()?.forEach { file ->
            if(file.isDirectory) return@forEach
            if(file.extension != "jar") return@forEach

            try {
                val jar = JarFile(file)
                val entry = jar.getJarEntry("module.json")
                if(entry == null) return@forEach
                val inputStream = jar.getInputStream(entry)
                val description = Json.decodeFromString<ModuleDescription>(inputStream.reader().readText()) ?: return@forEach

                if(!loadModule(description)){
                    println("Failed to load module ${description.name}")
                    return@forEach
                }
                println("Module ${description.name} loaded")

            }catch (e: Exception) {
                println("Error while loading module ${file.name}")
                e.printStackTrace()
            }
        }
    }

    override fun getModules(): List<Module> {
        return modules
    }

    override fun getModules(state: ModuleState): List<Module> {
        return modules.filter { it.state == state }
    }

    override fun getModule(name: String): Module? {
        return modules.firstOrNull { it.description.name == name }
    }

    override fun getModuleDescription(name: String): ModuleDescription? {
        return modules.firstOrNull { it.description.name == name }?.description
    }

    override fun loadModule(description: ModuleDescription): Boolean {
        val moduleClassLoader = ModuleClassLoader(this, description, javaClass.classLoader)
        val module = moduleClassLoader.loadClass() ?: return false

        modules.add(module)
        return true
    }

    override fun unloadModule(name: String): Boolean {
        val module = getModule(name) ?: return false
        module.onDisable(api)
        modules.remove(module)
        return true
    }

    override fun enableModules() {
        detectModules(File("modules"))
        modules.filter { it.state == ModuleState.LOADED }.forEach { it.onEnable(api) }
    }

    override fun disableModules() {
        modules.filter { it.state == ModuleState.LOADED || it.state == ModuleState.ENABLED }.forEach { it.onDisable(api) }
    }

}