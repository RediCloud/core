plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.3.11"
}

group = "net.dustrean.api.paper"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))

    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}