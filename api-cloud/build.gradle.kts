plugins {
    id("java")
}

group = "net.dustrean.api.cloud"

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))
}