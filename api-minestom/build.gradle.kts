import net.dustrean.libloader.plugin.LibraryLoader

plugins {
    kotlin("jvm") version "1.7.22"
}


the(LibraryLoader.LibraryLoaderConfig::class).apply {
    this.libraryFolder.set(project.projectDir.path + "/libraries")
    this.configurationName.set("runtimeClasspath")
}
val projects by extra(listOf("api", "api-cloud"))

dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
    compileOnly(project(":api-cloud"))

    compileOnly("com.github.Minestom.Minestom:Minestom:d37f40a1bb")
}

tasks {
    jar {
        manifest {
            attributes["Manifest-Version"] = "1.0"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}