plugins {
    kotlin("jvm") version "1.7.21"
}

group = "net.dustrean.api.cloud"

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))
}