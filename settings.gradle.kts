
include("api")
include("api-impl")
include("api-standalone")
include("api-cloud")
include("api-paper")
include("api-velocity")
include("api-minestom")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.redicloud.dev/releases")
    }
    val libloaderVersion: String by settings
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        id("dev.redicloud.libloader") version libloaderVersion
    }
}