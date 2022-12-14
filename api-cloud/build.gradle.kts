plugins {
    kotlin("jvm") version "1.7.22"
}

the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

val projects by extra(listOf("api"))
dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
}