plugins {
    id("java")
}

group = "net.dustrean.api.velocity"


repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    implementation(project(":api-cloud"))
    implementation(project(":api-impl"))
    implementation(project(":api"))
}