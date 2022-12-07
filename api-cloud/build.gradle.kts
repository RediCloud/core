plugins {
    kotlin("jvm") version "1.7.22"
}

val maven by extra(true)
val projects by extra(listOf("api"))
dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
}