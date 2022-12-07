plugins {
    kotlin("jvm") version "1.7.22"
}

val projects by extra(listOf("api"))

dependencies {
    compileOnly(project(":api"))
    testImplementation(project(":api"))
    implementation2("org.redisson:redisson:3.18.0")
    implementation2("com.google.guava:guava:31.1-jre")
}