plugins {
    kotlin("jvm") version "1.7.22"
    `maven-publish`
}
val maven: Boolean by extra(true)

group = "net.dustrean.api"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.redisson:redisson:3.18.0")
}

java {
    withSourcesJar()
    withJavadocJar()
}
