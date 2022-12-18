plugins {
    kotlin("jvm") version "1.7.22"
    `maven-publish`
}
val maven by extra(true)
repositories {
    mavenCentral()
}

the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

java {
    withSourcesJar()
    withJavadocJar()
}