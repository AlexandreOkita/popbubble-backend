plugins {
    kotlin("jvm") version "1.5.20"
    application
}

group = "okita.popbubble.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

val ktor_version: String by project
val logback_version: String by project

tasks.create("stage") {
    dependsOn("installDist")
}

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}