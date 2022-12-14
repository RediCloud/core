plugins {
    kotlin("jvm") version "1.7.22"
}


val projects by extra(listOf("api"))

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    shade(project(":api-cloud"))
    shade(project(":api-impl"))
    compileOnly(project(":api"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}