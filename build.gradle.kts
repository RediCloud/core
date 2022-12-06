import net.dustrean.libloader.plugin.LibraryLoader.LibraryLoaderConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.22"
    id("net.dustrean.libloader") version "1.1.0"
}
if (System.getenv("CI") == "true")
    tasks.replace("build").dependsOn("buildInOrder")
tasks.create("buildInOrder") {
    dependsOn(
        ":api:build", ":api:publishApiPublicationToDustreanRepository",
        ":api-impl:build", ":api-impl:publishApi_implPublicationToDustreanRepository",
        ":api-cloud:build", ":api-cloud:publishApi_cloudPublicationToDustreanRepository",
        ":api-minestom:build",
        ":api-velocity:build",
        ":api-paper:build"
    )
}
allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "net.dustrean.libloader")
    apply(plugin = "maven-publish")

    group = "net.dustrean.api"
    version = "1.0.0-SNAPSHOT"

    val isCi by extra(System.getenv("CI") == "true")


    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.dustrean.net/snapshots/")
            credentials {
                username = findProperty("DUSTREAN_REPO_USERNAME") as String? ?: System.getenv("DUSTREAN_REPO_USERNAME")
                password = findProperty("DUSTREAN_REPO_PASSWORD") as String? ?: System.getenv("DUSTREAN_REPO_PASSWORD")
            }
        }
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
    afterEvaluate {
        if (try {
                extra.get("maven")
            } catch (e: Exception) {
                false
            } == true
        )
            (extensions["publishing"] as PublishingExtension).apply {
                repositories {
                    maven {
                        name = "dustrean"
                        url = uri(
                            if (!project.version.toString()
                                    .endsWith("-SNAPSHOT")
                            ) "https://repo.dustrean.net/releases" else "https://repo.dustrean.net/snapshots"
                        )
                        credentials {
                            username = findProperty("DUSTREAN_REPO_USERNAME") as String?
                                ?: System.getenv("DUSTREAN_REPO_USERNAME")
                            password = findProperty("DUSTREAN_REPO_PASSWORD") as String?
                                ?: System.getenv("DUSTREAN_REPO_PASSWORD")
                        }
                        authentication {
                            create<BasicAuthentication>("basic")
                        }
                    }
                }
                publications {
                    create<MavenPublication>(project.name.replace("-", "_")) {
                        groupId = project.group.toString()
                        artifactId = project.name
                        version = "${project.version}"
                        from(components["java"])
                    }
                }
            }
        the(LibraryLoaderConfig::class).apply {
            val projects: List<String?> = try {
                extra.get("projects") as List<String>?
            } catch (e: Exception) {
                null
            } ?: return@apply
            projects.forEach {
                if (it.isNullOrBlank()) return@forEach
                val tProject = project(":$it")
                println("Adding ${tProject.group}:${tProject.name}:${tProject.version} to ${project.name}")
                notationList.add("${tProject.group}:${tProject.name}:${tProject.version}")
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
    }
}