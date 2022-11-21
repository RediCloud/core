import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "com.dustrean"
version = "1.0-SNAPSHOT"

allprojects {

    repositories {
        mavenCentral()
    }

    dependencies {

    }

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}