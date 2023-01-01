package net.dustrean.api.module

import com.google.gson.Gson
import net.dustrean.api.ICoreAPI
import net.dustrean.api.utils.getModuleFolder
import net.dustrean.libloader.boot.Bootstrap
import net.dustrean.libloader.boot.loaders.URLClassLoaderJarLoader
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

class ModuleManager(
    var api: ICoreAPI
) : IModuleManager {

    private val logger = LoggerFactory.getLogger(ModuleManager::class.java)
    private val gson = Gson()

    private val modules = mutableListOf<Module>()

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

        val loader = URLClassLoader(arrayListOf(file.toURI().toURL()).toTypedArray(), javaClass.classLoader)

        if(description.mainClasses[api.getNetworkComponentInfo().type] == null) return

        val module =
            loader.loadClass(description.mainClasses[api.getNetworkComponentInfo().type]).newInstance() as Module

        try {
            Bootstrap().apply(ModuleClassLoader(loader), loader, loader)
        } catch (e: Throwable) {
            logger.info("No libloader implementation found, continuing", e)
        }

        module::class.java.superclass.getDeclaredField("description").apply {
            isAccessible = true
            set(module, description)
            isAccessible = false
        }

        try {
            module.onLoad(api)
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
                module.onDisable(api)
                logger.info("Disabled module ${module.description.name}")
            } catch (e: Exception) {
                logger.error("Failed to disable module ${module.description.name}", e)
            }
        }
        modules.remove(module)
        module.state = ModuleState.DISABLED
        logger.info("Unloaded module ${module.description.name}")
        return true
    }

    override fun unloadModule(name: String): Boolean {
        val module = getModule(name) ?: return false
        if (module.state == ModuleState.ENABLED) {
            try {
                module.onDisable(api)
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
                it.onEnable(api)
                logger.info("Enabled module ${it.description.name}")
            } catch (e: Exception) {
                logger.error("Failed to enable module ${it.description.name}", e)
            }
            it.state = ModuleState.ENABLED
        }
    }

    override fun disableModules() {
        modules.filter { it.state == ModuleState.LOADED || it.state == ModuleState.ENABLED }.forEach {
            try {
                it.onDisable(api)
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

}