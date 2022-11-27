rootProject.name = "core"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

include("api")
include("api-impl")
include("api-paper")
include("api-cloud")
include("api-velocity")
include("api-minestom")