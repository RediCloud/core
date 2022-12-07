plugins {
    kotlin("jvm") version "1.7.22"
    id("io.papermc.paperweight.userdev") version "1.3.11"
}

val projects by extra(listOf("api", "api-cloud"))

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
    compileOnly(project(":api-cloud"))

    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}