group = "com.gradlejustin"
description = "API demo for Gradle Build Scans"

plugins {
    `java-library`
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.gradlejustin.apidemo.BuildScanExtractor")
}

dependencies {
    implementation("org.json:json:20230227")
    implementation("io.prometheus:simpleclient:0.16.0")
    implementation("io.prometheus:simpleclient_hotspot:0.16.0")
    implementation("io.prometheus:simpleclient_httpserver:0.16.0")
    implementation("io.prometheus:simpleclient_pushgateway:0.16.0")
}

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")
    if (project.name.equals("kotlin")) {
        manifest.attributes["Main-Class"] = "com.gradlejustin.apidemo.BuildScanExtractorKt"
    } else {
        manifest.attributes["Main-Class"] = "com.gradlejustin.apidemo.BuildScanExtractor"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath) //dependsOn(sourceSets.main.get().runtimeClasspath)
    from({
        configurations.implementation.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        //sourceSets.main.get().runtimeClasspath.filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

