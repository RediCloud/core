package net.dustrean.api.module

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.dustrean.api.CoreAPI
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

class ModuleManager(
    var api: CoreAPI
) : IModuleManager {

    private val logger = LoggerFactory.getLogger(ModuleManager::class.java)

    val modules= mutableListOf<Module>()

    init {
        logger.info("Initializing module manager")
        enableModules()
    }

    fun detectModules(folder: File) {
        folder.listFiles()?.forEach { file ->
            if(file.isDirectory) return@forEach
            if(file.extension != "jar") return@forEach

            try {
                val jar = JarFile(file)
                val entry = jar.getJarEntry("module.json")
                if(entry == null)  {
                    logger.error("Module ${file.name} does not have a module.json file!")
                    return@forEach
                }
                val inputStream = jar.getInputStream(entry)
                val description = Json.decodeFromString<ModuleDescription>(inputStream.reader().readText())

                description::class.java.getDeclaredField("file").apply {
                    isAccessible = true
                    set(description, file)
                    isAccessible = false
                }

                if(!loadModule(description)){
                    logger.error("Failed to load module ${file.name}")
                    return@forEach
                }
               logger.info("Loaded module ${file.name}")

            }catch (e: Exception) {
                logger.error("Failed to load module ${file.name}", e)
                e.printStackTrace()
            }
        }
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

        val loader = URLClassLoader(arrayListOf(description.file.toURI().toURL()).toTypedArray(), javaClass.classLoader)

        val module = loader.loadClass(description.mainClasses[api.getNetworkComponentInfo().type]).newInstance() as Module

        module::class.java.getDeclaredField("description").apply {
            isAccessible = true
            set(module, description)
            isAccessible = false
        }

        module.onLoad(api)

        module.state = ModuleState.LOADED
        modules.add(module)

        return true
    }

    override fun unloadModule(module: Module): Boolean {
        module.onDisable(api)
        modules.remove(module)
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

    override fun reloadModules() {
        disableModules()
        enableModules()
    }

}