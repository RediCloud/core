plugins {
    kotlin("jvm") version "1.7.22"
}

val projects by extra(listOf("api"))

val default: Configuration by configurations.creating

configurations.compileClasspath.get().extendsFrom(default)

the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class).apply {
    this.configurationName.set("default")
}
dependencies {
    compileOnly(project(":api"))
    testImplementation(project(":api"))
    add("default","org.redisson:redisson:3.18.0")
    add("default", "com.google.guava:guava:31.1-jre")
}