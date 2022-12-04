plugins {
    kotlin("jvm") version "1.7.22"
}

group = "net.dustrean.api.cloud"

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))
}