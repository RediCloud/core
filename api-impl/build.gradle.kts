import net.dustrean.libloader.plugin.LibraryLoader

plugins {
    kotlin("jvm") version "1.7.22"
}


the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

val projects by extra(listOf("api"))

val libloaderVersion: String by project
dependencies {
    compileOnly("net.dustrean.libloader:libloader-bootstrap:${libloaderVersion}")
    compileOnly(project(":api"))
    implementation2("com.google.guava:guava:31.1-jre")

    testCompileOnly(project(":api"))
    testCompileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testCompileOnly(kotlin("reflect"))
    testCompileOnly("com.fasterxml.jackson.core:jackson-annotations:2.14.0")
    testCompileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
}