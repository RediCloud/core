import net.dustrean.libloader.plugin.LibraryLoader

apply(plugin = "org.jetbrains.kotlin.jvm")

val maven by extra(true)
val projects by extra(listOf("api"))
the(LibraryLoader.LibraryLoaderConfig::class).mainClass.set("net.dustrean.api.standalone.MainKt")
dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
}
