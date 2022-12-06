plugins {
    kotlin("jvm") version "1.7.22"
}

group = "net.dustrean.api.cloud"
val maven by extra(true)
val projects by extra(listOf("api", "api-impl"))
dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
    compileOnly("eu.cloudnetservice.cloudnet:driver:4.0.0-RC5")
    compileOnly("eu.cloudnetservice.cloudnet:bridge:4.0.0-RC5")
    compileOnly("eu.cloudnetservice.cloudnet:wrapper-jvm:4.0.0-RC5")
    compileOnly("eu.cloudnetservice.cloudnet:cloudperms:4.0.0-RC5")
}