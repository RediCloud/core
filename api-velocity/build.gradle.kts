plugins {
    kotlin("jvm") version "1.7.22"
}


val projects by extra(listOf("api", "api-impl", "api-cloud"))

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    compileOnly(project(":api-cloud"))
    compileOnly(project(":api-impl"))
    compileOnly(project(":api"))
}