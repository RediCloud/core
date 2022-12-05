plugins {
    kotlin("jvm") version "1.7.22"
    id("io.papermc.paperweight.userdev") version "1.3.11"
}

group = "net.dustrean.api.paper"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    if (!net.dustrean.Functions.isCi()) {
        implementation(project(":api"))
        implementation(project(":api-impl"))
        implementation(project(":api-cloud"))
    } else {
        implementation("net.dustrean.api:api:1.0.0")
        implementation("net.dustrean.api:api-impl:1.0.0")
        implementation("net.dustrean.api:api-cloud:1.0.0")
    }

    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}