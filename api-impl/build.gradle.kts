plugins {
    kotlin("jvm") version "1.7.22"
}

val projects by extra(listOf("api"))

val implementation2: Configuration by configurations.creating

configurations.compileClasspath.get().extendsFrom(implementation2)

the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class).apply {
    this.configurationName.set("implementation2")
}
dependencies {
    compileOnly(project(":api"))
    testImplementation(project(":api"))
    add("implementation2","org.redisson:redisson:3.18.0")
    add("implementation2", "com.google.guava:guava:31.1-jre")
}