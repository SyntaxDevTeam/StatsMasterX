plugins {
    kotlin("jvm") version "2.1.0-Beta1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "pl.syntaxdevteam"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("dev.folia:folia-api:1.20.6-R0.1-SNAPSHOT")

    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2-SNAPSHOT")
    compileOnly("org.postgresql:postgresql:42.7.3")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
