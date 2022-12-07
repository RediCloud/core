plugins {
    kotlin("jvm") version "1.7.22"
}

val projects by extra(listOf("api"))
dependencies {
    compileOnly(project(":api"))
    testImplementation(project(":api"))
    implementation("org.redisson:redisson:3.18.0")
    implementation("com.google.guava:guava:31.1-jre")
}