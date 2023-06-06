group = "com.gradlejustin"
description = "API demo for Gradle Build Scans"

plugins {
    `java-library`
    application
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
