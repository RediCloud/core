import net.dustrean.libloader.plugin.LibraryLoader

plugins {
    kotlin("jvm")
}


the(LibraryLoader.LibraryLoaderConfig::class).apply {
    this.libraryFolder.set(project.projectDir.path + "/libraries")
    this.configurationName.set("runtimeClasspath")
}
val projects by extra(listOf("api"))

dependencies {
    compileOnly(project(":api"))
    shade(project(":api-impl"))
    shade(project(":api-cloud"))

    compileOnly("com.github.Minestom.Minestom:Minestom:1.19.3-SNAPSHOT")
}

tasks {
    jar {
        manifest {
            attributes["Manifest-Version"] = "1.0"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}