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
        maven {
            name = "dustrean"
            url = uri("https://repo.dustrean.net/releases")
            credentials {
                username = System.getenv("DUSTREAN_REPO_USERNAME")
                password = System.getenv("DUSTREAN_REPO_PASSWORD")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}