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
val projects by extra(listOf("api", "api-impl", "api-cloud"))

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":api-impl"))
    compileOnly(project(":api-cloud"))

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