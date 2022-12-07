plugins {
    kotlin("jvm") version "1.7.22"
    `maven-publish`
}
val maven by extra(true)
repositories {
    mavenCentral()
}

dependencies {
    implementation2("org.redisson:redisson:3.18.0")
}

java {
    withSourcesJar()
    withJavadocJar()
}