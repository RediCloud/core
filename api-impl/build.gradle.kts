import net.dustrean.libloader.plugin.LibraryLoader

plugins {
    kotlin("jvm")
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

    implementation2("org.slf4j:slf4j-api:2.0.6")
    implementation2("org.slf4j:slf4j-simple:2.0.6")
    implementation2("org.apache.logging.log4j:log4j-slf4j2-impl:2.19.0")
}