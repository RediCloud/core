plugins {
    kotlin("jvm") version "1.7.21"
}

group = "net.dustrean.api"

dependencies {
    implementation(project(":api"))
    implementation("org.redisson:redisson:3.18.0")
    implementation("com.google.guava:guava:31.1-jre")
}