plugins {
    kotlin("jvm") version "1.7.21"
    `maven-publish`
}

val projectVersion = "1.0.0"
val snapshot = true
group = "net.dustrean.api"
version = "$projectVersion${if (snapshot) "-SNAPSHOT" else "-RELEASE"}"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.redisson:redisson:3.18.0")
}

publishing {
    repositories {
        maven {
            name = "dustrean"
            url = uri( if (!snapshot) "https://repo.dustrean.net/releases" else "https://repo.dustrean.net/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("reposilite-repository") {
            groupId = "net.dustrean"
            artifactId = "api"
            version = "$projectVersion${if (snapshot) "-SNAPSHOT" else "-RELEASE"}"
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}
