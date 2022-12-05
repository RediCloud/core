import net.dustrean.libloader.plugin.LibraryLoader

plugins {
    kotlin("jvm") version "1.7.22"
}

group = "net.dustrean.api.minestom"

the(LibraryLoader.LibraryLoaderConfig::class).apply {
    this.libraryFolder.set(project.projectDir.path + "/libraries")
    this.configurationName.set("runtimeClasspath")
    this.mainClass.set("net.dustrean.api.minestom.bootstrap.MinestomBootstrap")
}

dependencies {
    if (!net.dustrean.Functions.isCi()) {
        implementation(project(":api"))
        implementation(project(":api-impl"))
        implementation(project(":api-cloud"))
    } else {
        implementation("net.dustrean.api:api:1.0.0")
        implementation("net.dustrean.api:api-impl:1.0.0")
        implementation("net.dustrean.api:api-cloud:1.0.0")
    }
    implementation("com.github.Minestom.Minestom:Minestom:d37f40a1bb")
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "net.dustrean.api.minestom.bootstrap.MinestomBootstrap", "Manifest-Version" to "1.0"
                )
            )
        }
    }
}