plugins {
    kotlin("jvm") version "1.7.21"
}

group = "com.dustrean.api"

dependencies {
    implementation(project(":api"))
    implementation("org.redisson:redisson:3.18.0")
}