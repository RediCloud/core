plugins {
    kotlin("jvm") version "1.7.21"
    signing
    `maven-publish`
}

val projectVersion = "1.0.0"
val snapshot = false
group = "net.dustrean.api"
version = "$projectVersion${if (snapshot) "-SNAPSHOT" else "-RELEASE"}"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            name = "dustrean"
            url = uri( if (!snapshot)"https://repo.dustrean.net/releases" else "https://repo.dustrean.net/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.dustrean"
            artifactId = "api"
            version = "$projectVersion${if (snapshot) "-SNAPSHOT" else "-RELEASE"}"
            from(components["java"])
        }
    }
}


signing {
    sign(publishing.publications)
}

java {
    withSourcesJar()
    withJavadocJar()
}