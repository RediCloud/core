package com.dustrean.api.module

import com.google.common.io.ByteStreams
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.security.CodeSource
import java.util.jar.JarFile
import java.util.jar.Manifest

class ModuleClassLoader(
    private val manager: ModuleManager,
    private val description: ModuleDescription,
    parent: ClassLoader?
) : URLClassLoader(arrayOf(description.file.toURI().toURL()), parent) {

    companion object {
        val loaders: MutableList<ModuleClassLoader> = ArrayList()
    }

    private val jarFile: JarFile = JarFile(description.file)
    private val manifest: Manifest = jarFile.manifest
    private val url: URL = description.file.toURI().toURL()

    init {
        loaders.add(this)
    }

    @Throws(Exception::class)
    fun loadClass(): Module? {
        val bootstrap: Class<*>? = loadClass(description.mainClasses[manager.api.getNetworkComponentInfo().type])
        if(bootstrap == null) return null
        val module: Module = bootstrap.newInstance() as Module
        module.classLoader = this
        module.description = description
        module.onLoad(manager.api)
        module.state = ModuleState.LOADED
        return module
    }

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String?, resolve: Boolean): Class<*>? {
        return loadClass0(name, resolve)
    }

    @Throws(ClassNotFoundException::class)
    fun loadClass0(name: String?, resolve: Boolean): Class<*>? {
        try {
            val result = super.loadClass(name, resolve)
            if (result != null) {
                return result
            }
        } catch (ignore: ClassNotFoundException) {
        }
        for (loader in loaders) {
            if (loader === this) continue
            return loader.loadClass0(name, resolve)
        }
        throw ClassNotFoundException(name)
    }

    @Throws(ClassNotFoundException::class)
    override fun findClass(name: String): Class<*>? {
        val path = name.replace('.', '/') + ".class"
        val entry = jarFile.getJarEntry(path)
        if (entry != null) {
            var classBytes: ByteArray
            try {
                jarFile.getInputStream(entry).use { `is` -> classBytes = ByteStreams.toByteArray(`is`) }
            } catch (ex: IOException) {
                throw ClassNotFoundException(name, ex)
            }
            val dot = name.lastIndexOf('.')
            if (dot != -1) {
                val pkgName = name.substring(0, dot)
                if (getPackage(pkgName) == null) {
                    try {
                        definePackage(pkgName, manifest, url)
                    } catch (ex: IllegalArgumentException) {
                        checkNotNull(getPackage(pkgName)) { "Cannot find package $pkgName" }
                    }
                }
            }
            val signers = entry.codeSigners
            val source = CodeSource(url, signers)
            return defineClass(name, classBytes, 0, classBytes.size, source)
        }
        return super.findClass(name)
    }

}