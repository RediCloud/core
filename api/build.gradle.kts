plugins {
    kotlin("jvm")
    `maven-publish`
}
val maven by extra(true)
repositories {
    mavenCentral()
}

dependencies{
    implementation2("org.mindrot:jbcrypt:0.4")
}

the(net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class)
    .doBootstrapShade.set(false)

java {
    withSourcesJar()
    withJavadocJar()
}