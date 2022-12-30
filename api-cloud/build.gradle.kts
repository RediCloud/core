plugins {
    kotlin("jvm")
}

the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

val projects by extra(listOf("api"))
dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
}