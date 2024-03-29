plugins {
    kotlin("jvm")
    id("io.papermc.paperweight.userdev") version "1.3.11"
}

val projects by extra(listOf("api"))

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
    shade(project(":api-cloud"))

    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.19.2-R0.1-SNAPSHOT")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}