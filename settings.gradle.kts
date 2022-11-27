rootProject.name = "core"

include("api")
include("api-impl")
include("api-paper")
include("api-cloud")
include("api-velocity")
include("api-minestom")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}