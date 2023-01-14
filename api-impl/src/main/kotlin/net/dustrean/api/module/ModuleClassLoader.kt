package net.dustrean.api.module

import net.dustrean.libloader.boot.JarLoader
import java.net.URL
import java.net.URLClassLoader

class ModuleClassLoader(
    urls: Array<URL>,
    parent: ClassLoader
): JarLoader, URLClassLoader(urls, parent) {
    override fun load(javaFile: URL?) {
        addURL(javaFile)
    }
}