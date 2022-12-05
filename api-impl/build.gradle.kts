import org.jetbrains.kotlin.gradle.utils.`is`

plugins {
    kotlin("jvm") version "1.7.22"
}
val maven: Boolean by extra(true)


group = "net.dustrean.api"
version = "1.0.0-SNAPSHOT"
dependencies {
    if (!net.dustrean.Functions.isCi())
        implementation(project(":api"))
    else
        implementation("net.dustrean.api:api:1.0.0")
    implementation("org.redisson:redisson:3.18.0")
    implementation("com.google.guava:guava:31.1-jre")
}