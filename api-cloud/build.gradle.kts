import org.jetbrains.kotlin.gradle.targets.js.isTeamCity

plugins {
    kotlin("jvm") version "1.7.22"
}
val maven: Boolean by extra(true)
version = "1.0.0-SNAPSHOT"
group = "net.dustrean.api.cloud"
dependencies {
    if (!net.dustrean.Functions.isCi()) {
        implementation(project(":api"))
        implementation(project(":api-impl"))
    } else {
        implementation("net.dustrean.api:api:1.0.0")
        implementation("net.dustrean.api:api-impl:1.0.0")
    }
}