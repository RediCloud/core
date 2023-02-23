plugins {
    kotlin("jvm")
    `maven-publish`
}
val maven by extra(true)
repositories {
    mavenCentral()
}

val libloaderVersion: String by project
dependencies{
    implementation2("org.mindrot:jbcrypt:0.4")
    compileOnly("dev.redicloud.libloader:libloader-bootstrap:${libloaderVersion}")
}

the(dev.redicloud.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

java {
    withSourcesJar()
    withJavadocJar()
}