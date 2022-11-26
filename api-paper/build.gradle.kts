plugins {
    id("java")
}

group = "net.dustrean.api.paper"

repositories {

}

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))
}