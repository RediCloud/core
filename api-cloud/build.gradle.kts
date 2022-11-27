plugins {
    id("java")
}

group = "net.dustrean.api.cloud"

repositories {

}

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))
}