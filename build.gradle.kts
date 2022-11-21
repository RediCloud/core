import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.lombok") version "1.7.21"
    id("io.freefair.lombok") version "5.3.0"
}

group = "com.dustrean"
version = "1.0-SNAPSHOT"

allprojects {

    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
    }

    dependencies {

    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

}
