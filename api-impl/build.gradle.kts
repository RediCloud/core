plugins {
    kotlin("jvm") version "1.7.22"
}

val projects by extra(listOf("api"))

dependencies {
    compileOnly(project(":api"))
    implementation2("org.redisson:redisson:3.18.0")
    implementation2("com.google.guava:guava:31.1-jre")

    testImplementation(project(":api"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    testImplementation(kotlin("reflect"))
    testImplementation("com.fasterxml.jackson.core:jackson-annotations:2.14.0")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
}