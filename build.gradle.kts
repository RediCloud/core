import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    kotlin("jvm")
    id("dev.redicloud.libloader")
}

if (System.getenv("CI") == "true")
    tasks.replace("build").dependsOn("buildInOrder")
tasks.create("buildInOrder") {
    dependsOn(
        ":api:build", ":api:publishApiPublicationToRedicloudRepository",
        ":api-impl:build",
        ":api-standalone:build",
        ":api-cloud:build",
        ":api-minestom:build",
        ":api-velocity:build",
        ":api-paper:build"
    )
}
allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "dev.redicloud.libloader")
    apply(plugin = "maven-publish")

    group = "dev.redicloud.api"
    version = "1.0.0-SNAPSHOT"

    val isCi by extra(System.getenv("CI") == "true")

    val implementation2 by configurations.creating
    configurations.compileClasspath.get().extendsFrom(implementation2)

    fun DependencyHandlerScope.implementation2(dependencyNotation: Any): Dependency? =
        add("implementation2", dependencyNotation)

    repositories {
        mavenCentral()
        maven ("https://repo.redicloud.dev/snapshots/")
        gradlePluginPortal()
        maven("https://repo.cloudnetservice.eu/repository/releases/")
        maven("https://jitpack.io")
        maven("https://repo.redicloud.dev/releases/")
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
                        name = "redicloud"
                        url = uri(
                            if (!project.version.toString()
                                    .endsWith("-SNAPSHOT")
                            ) "https://repo.redicloud.dev/releases" else "https://repo.redicloud.dev/snapshots"
                        )
                        credentials {
                            username = findProperty("REDI_CLOUD_REPO_USERNAME") as String?
                                ?: System.getenv("REDI_CLOUD_REPO_USERNAME")
                            password = findProperty("REDI_CLOUD_REPO_PASSWORD") as String?
                                ?: System.getenv("REDI_CLOUD_REPO_PASSWORD")
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
        the(dev.redicloud.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class).apply {
            val projects: List<String?> = try {
                extra.get("projects") as List<String>?
            } catch (e: Exception) {
                null
            } ?: return@apply
            projects.forEach {
                if (it.isNullOrBlank()) return@forEach
                val tProject = project(":$it")
                notationList.add("${tProject.group}:${tProject.name}:${tProject.version}")
            }
        }
    }

    the(dev.redicloud.libloader.plugin.LibraryLoader.LibraryLoaderConfig::class).apply {
        this.libraryFolder.set(project.projectDir.path + "/libraries")
        this.configurationName.set("implementation2")
    }

    dependencies {
        implementation2(kotlin("reflect"))
        implementation2("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation2("com.fasterxml.jackson.core:jackson-annotations:2.14.0")
        implementation2("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
        implementation2("org.redisson:redisson:3.19.0")
        compileOnly("eu.cloudnetservice.cloudnet:driver:4.0.0-RC7")
        compileOnly("eu.cloudnetservice.cloudnet:bridge:4.0.0-RC7")
        compileOnly("eu.cloudnetservice.cloudnet:wrapper-jvm:4.0.0-RC7")
        compileOnly("eu.cloudnetservice.cloudnet:cloudperms:4.0.0-RC7")
        compileOnly("eu.cloudnetservice.cloudnet:platform-inject-api:4.0.0-RC7")
        annotationProcessor("eu.cloudnetservice.cloudnet:platform-inject-processor:4.0.0-RC7")

        implementation2("net.kyori:adventure-api:4.12.0")
        implementation2("net.kyori:adventure-text-serializer-gson:4.12.0")
        implementation2("net.kyori:adventure-text-serializer-gson-legacy-impl:4.12.0")
        implementation2("net.kyori:adventure-text-serializer-plain:4.12.0")
        implementation2("net.kyori:adventure-text-serializer-legacy:4.12.0")
        implementation2("net.kyori:adventure-text-minimessage:4.12.0")
        implementation2("net.kyori:adventure-platform-api:4.1.2")
        implementation2("net.kyori:adventure-platform-facet:4.1.2")
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