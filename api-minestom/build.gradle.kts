import net.dustrean.libloader.plugin.LibraryLoader

plugins {
    kotlin("jvm") version "1.7.22"
}


the(LibraryLoader.LibraryLoaderConfig::class).apply {
    this.libraryFolder.set(project.projectDir.path + "/libraries")
    this.configurationName.set("runtimeClasspath")
    this.mainClass.set("net.dustrean.api.minestom.bootstrap.MinestomBootstrap")
}
val projects by extra(listOf("api"))

dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
    shade(project(":api-cloud"))

    implementation2("com.github.Minestom.Minestom:Minestom:d37f40a1bb")
}

tasks {
    jar {
        manifest {
            attributes["Manifest-Version"] = "1.0"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}