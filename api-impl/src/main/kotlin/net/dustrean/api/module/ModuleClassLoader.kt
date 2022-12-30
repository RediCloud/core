package net.dustrean.api.module

import net.dustrean.libloader.boot.JarLoader
import java.net.URL
import java.net.URLClassLoader

class ModuleClassLoader(
    private val classLoader: URLClassLoader
): JarLoader {
    override fun load(javaFile: URL?) {
        classLoader.javaClass.getDeclaredMethod("addURL", URL::class.java).apply {
            isAccessible = true
        }.invoke(classLoader, javaFile)
    }
}