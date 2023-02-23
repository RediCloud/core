import dev.redicloud.libloader.plugin.LibraryLoader

val projects by extra(listOf("api"))
the(LibraryLoader.LibraryLoaderConfig::class).mainClass.set("dev.redicloud.api.standalone.MainKt")
dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
}
tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.WARN
}