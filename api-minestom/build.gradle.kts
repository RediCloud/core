plugins {
    id("java")
}

group = "net.dustrean.api.minestom"

dependencies {
    implementation(project(":api"))
    implementation(project(":api-impl"))
    implementation(project(":api-cloud"))

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