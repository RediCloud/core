package dev.redicloud.api.module

import dev.redicloud.libloader.boot.JarLoader
import java.net.URL
import java.net.URLClassLoader

class ModuleClassLoader(
    name: String,
    urls: Array<URL>,
    parent: ClassLoader
): JarLoader, URLClassLoader("module_$name", urls, parent) {

    override fun load(javaFile: URL?) {
        addURL(javaFile)
    }

}