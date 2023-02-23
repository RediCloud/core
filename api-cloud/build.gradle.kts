plugins {
    kotlin("jvm")
}

the(dev.redicloud.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

val projects by extra(listOf("api"))
dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
}