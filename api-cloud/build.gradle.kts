plugins {
    kotlin("jvm") version "1.7.22"
}

group = "net.dustrean.api.cloud"
val maven by extra(true)
val projects by extra(listOf("api", "api-impl"))
dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
}