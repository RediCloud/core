plugins {
    kotlin("jvm") version "1.7.22"
}

group = "net.dustrean.api.velocity"


repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    if (!net.dustrean.Functions.isCi()) {
        implementation(project(":api"))
        implementation(project(":api-impl"))
        implementation(project(":api-cloud"))
    } else {
        implementation("net.dustrean.api:api:1.0.0")
        implementation("net.dustrean.api:api-impl:1.0.0")
        implementation("net.dustrean.api:api-cloud:1.0.0")
    }
}