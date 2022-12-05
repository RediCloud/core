import net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.22"
    id("net.dustrean.libloader") version "1.0.0"
}

group = "net.dustrean"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "net.dustrean.libloader")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.cloudnetservice.eu/repository/releases/")
        maven("https://jitpack.io")
        maven {
            url = uri("https://repo.dustrean.net/releases/")
            credentials {
                username = findProperty("DUSTREAN_REPO_USERNAME") as String? ?: System.getenv("DUSTREAN_REPO_USERNAME")
                password = findProperty("DUSTREAN_REPO_PASSWORD") as String? ?: System.getenv("DUSTREAN_REPO_PASSWORD")
            }
        }
    }

    the(LibraryLoaderConfig::class).apply {
        this.libraryFolder.set(project.projectDir.path + "/libraries")
        this.configurationName.set("runtimeClasspath")
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
        implementation(kotlin("reflect"))

        implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.0")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")

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
        create("buildAndPublishInOrder") {
            dependsOn(
                ":api:buildAndPublish", // Correct order
                ":api-impl:buildAndPublish",
                ":api-cloud:buildAndPublish",
                ":api-minestom:build", // Don't need to publish these
                ":api-paper:build",
                ":api-velocity:build"
            )
        }
        if (net.dustrean.Functions.isCi())
            replace("build").doFirst {
                finalizedBy("buildAndPublishInOrder")
            }
    }
    afterEvaluate {
        if (try {
                extra.get("maven") as Boolean?
            } catch (e: Exception) {
                false
            } == true
        )
            project.extensions.configure<PublishingExtension>("publishing") {
                repositories {
                    maven {
                        name = "dustrean"
                        url =
                            uri(
                                "https://repo.dustrean.net/${
                                    if (project.version.toString()
                                            .endsWith("-SNAPSHOT")
                                    ) "snapshots" else if (project.version.toString()
                                            .endsWith("-RELEASE")
                                    ) "releases" else throw NullPointerException("Version has to end with either -SNAPSHOT or -RELEASE\nProject: ${project.name}")
                                }/"
                            )
                        credentials {
                            username =
                                findProperty("DUSTREAN_REPO_USERNAME") as String?
                                    ?: System.getenv("DUSTREAN_REPO_USERNAME")
                            password =
                                findProperty("DUSTREAN_REPO_PASSWORD") as String?
                                    ?: System.getenv("DUSTREAN_REPO_PASSWORD")
                        }
                        authentication {
                            create<BasicAuthentication>("basic")
                        }
                    }
                }
                publications {
                    create<MavenPublication>("dustrean${project.name.capitalize()}") {
                        groupId = "${project.group}"
                        artifactId = project.name
                        version = "${project.version}"
                        from(components["java"])
                    }
                }
            }
        tasks.register("buildAndPublish") {
            dependsOn("build", "publishDustrean${project.name.capitalized()}ToDustreanRepository")
        }
    }
}