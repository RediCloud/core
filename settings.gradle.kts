
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
        maven {
            name = "dustrean"
            url = uri("https://repo.dustrean.net/releases")
            credentials {
                val DUSTREAN_REPO_USERNAME: String? by settings
                val DUSTREAN_REPO_PASSWORD: String? by settings
                username = DUSTREAN_REPO_USERNAME ?: System.getenv("DUSTREAN_REPO_USERNAME")
                password = DUSTREAN_REPO_PASSWORD ?: System.getenv("DUSTREAN_REPO_PASSWORD")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    val libloaderVersion: String by settings
    plugins {
        kotlin("jvm") version "1.7.22"
        kotlin("plugin.serialization") version "1.7.22"
        id("net.dustrean.libloader") version libloaderVersion
    }
}