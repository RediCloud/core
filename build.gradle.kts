import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
}

group = "net.dustrean"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.cloudnetservice.eu/repository/releases/")
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

        implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.0")

        compileOnly("eu.cloudnetservice.cloudnet:driver:4.0.0-RC5")
        compileOnly("eu.cloudnetservice.cloudnet:bridge:4.0.0-RC5")
        compileOnly("eu.cloudnetservice.cloudnet:wrapper-jvm:4.0.0-RC5")
        compileOnly("eu.cloudnetservice.cloudnet:cloudperms:4.0.0-RC5")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }

        withType<JavaCompile> {
            options.release.set(17)
            options.encoding = "UTF-8"
        }
    }
}