rootProject.name = "core"

include("api")
include("api-impl")
include("api-paper")
include("api-cloud")
include("api-velocity")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
