package dev.redicloud.api.module

import com.google.gson.Gson
import dev.redicloud.api.CoreAPI
import dev.redicloud.api.utils.getModuleFolder
import dev.redicloud.libloader.boot.Bootstrap
import dev.redicloud.libloader.boot.apply.impl.JarResourceLoader
import org.slf4j.LoggerFactory
import java.io.File
import java.util.jar.JarFile

class ModuleManager(
    private val core: CoreAPI
) : IModuleManager {

    private val logger = LoggerFactory.getLogger(ModuleManager::class.java)
    private val gson = Gson()

    private val modules = mutableListOf<Module>()
    private val moduleLoaders = mutableMapOf<Module, ModuleClassLoader>()

    fun detectModules(folder: File) {
        folder.listFiles()?.forEach { file ->
            if (file.isDirectory) return@forEach
            if (file.extension != "jar") return@forEach

            try {
                val jar = JarFile(file)
                val entry = jar.getJarEntry("module.json")
                if (entry == null) {
                    logger.error("Module ${file.name} does not have a module.json file!")
                    return@forEach
                }
                val inputStream = jar.getInputStream(entry)
                val description = gson.fromJson(inputStream.reader().readText(), ModuleDescription::class.java)

                if (!loadModule(description, file)) return@forEach
                logger.info("Loaded module ${file.name}")

            } catch (e: Exception) {
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

    override fun loadModule(description: ModuleDescription, file: File): Boolean {

        val loader = ModuleClassLoader(arrayOf(file.toURI().toURL()), listOf(this.javaClass.classLoader))

        if(description.mainClasses[core.networkComponentInfo.type] == null) return false

        val module =
            loader.loadClass(description.mainClasses[core.networkComponentInfo.type]).getDeclaredConstructor().newInstance() as Module

        try {
            Bootstrap().apply(loader, loader, JarResourceLoader(description.name, file))
        } catch (e: Throwable) {
            logger.info("No libloader implementation found, continuing", e)
        }



        module::class.java.superclass.getDeclaredField("description").apply {
            isAccessible = true
            set(module, description)
            isAccessible = false
        }

        try {
            module.onLoad(core)
            moduleLoaders[module] = loader
        } catch (e: Exception) {
            logger.error("Failed to load module ${description.name}", e)
            return false
        }

        module.state = ModuleState.LOADED
        modules.add(module)

        return true
    }

    override fun unloadModule(module: Module): Boolean {
        if (module.state == ModuleState.ENABLED) {
            try {
                module.onDisable(core)
                logger.info("Disabled module ${module.description.name}")
            } catch (e: Exception) {
                logger.error("Failed to disable module ${module.description.name}", e)
            }
        }
        moduleLoaders.remove(module)
        modules.remove(module)
        module.state = ModuleState.DISABLED
        logger.info("Unloaded module ${module.description.name}")
        return true
    }

    override fun unloadModule(name: String): Boolean {
        val module = getModule(name) ?: return false
        if (module.state == ModuleState.ENABLED) {
            try {
                module.onDisable(core)
                logger.info("Disabled module ${module.description.name}")
            } catch (e: Exception) {
                logger.error("Failed to disable module ${module.description.name}", e)
            }
        }
        module.state = ModuleState.DISABLED
        modules.remove(module)
        logger.info("Unloaded module $name")
        return true
    }

    override fun enableModules() {
        detectModules(getModuleFolder())
        modules.filter { it.state == ModuleState.LOADED }.forEach {
            try {
                it.onEnable(core)
                logger.info("Enabled module ${it.description.name}")
            } catch (e: Exception) {
                logger.error("Failed to enable module ${it.description.name}", e)
                it.state = ModuleState.ERROR
                return@forEach
            }
            it.state = ModuleState.ENABLED
        }
    }

    override fun disableModules() {
        modules.filter { it.state == ModuleState.LOADED || it.state == ModuleState.ENABLED }.forEach {
            try {
                it.onDisable(core)
                logger.info("Disabled module ${it.description.name}")
            } catch (e: Exception) {
                logger.error("Failed to disable module ${it.description.name}", e)
            }
            it.state = ModuleState.DISABLED
        }
    }

    override fun reloadModules() {
        logger.info("Reloading modules...")
        disableModules()
        enableModules()
    }

    override fun getModuleLoaders() = moduleLoaders.values.toList()

}