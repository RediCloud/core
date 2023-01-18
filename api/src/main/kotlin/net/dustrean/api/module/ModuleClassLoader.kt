package net.dustrean.api.module

import net.dustrean.libloader.boot.JarLoader
import java.net.URL
import java.net.URLClassLoader

class ModuleClassLoader(
    urls: Array<URL>,
    private val children: List<ClassLoader>
): JarLoader, URLClassLoader(urls) {

    override fun load(javaFile: URL?) {
        addURL(javaFile)
    }

    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        try {
            return super.loadClass(name, resolve)
        } catch (e: ClassNotFoundException) {
            for (child in children) {
                try {
                    return child.loadClass(name)
                } catch (e: ClassNotFoundException) {
                    continue
                }
            }
        }
        throw ClassNotFoundException(name)
    }
}