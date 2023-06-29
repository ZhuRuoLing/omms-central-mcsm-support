plugins {
    id("java")
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.zhuruoling"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://libraries.minecraft.net")
    }
    maven { url = uri("https://jitpack.io") }
    maven {
        url = uri("https://repo.opencollab.dev/maven-releases/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven{
        url = uri("https://jcenter.bintray.com/")
    }
}

dependencies {
    implementation("com.github.OhMyMinecraftServer:omms-central:master-SNAPSHOT")
    implementation("io.ktor:ktor-server-auth:2.0.2")
    implementation("io.ktor:ktor-server-auth-jvm:2.0.2")
    implementation("uk.org.lidalia:sysout-over-slf4j:1.0.2")
    implementation("org.jline:jline:3.21.0")
    implementation("com.mojang:brigadier:1.0.18")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.0.2")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.0.2")
    implementation("io.ktor:ktor-server-core-jvm:2.0.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.0.2")
    implementation("io.ktor:ktor-serialization-gson-jvm:2.0.2")
    implementation("io.ktor:ktor-server-netty-jvm:2.0.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")
    implementation("io.netty:netty-all:4.1.77.Final")
    implementation("com.github.oshi:oshi-core:6.1.6")
    implementation("net.java.dev.jna:jna:5.11.0")
    implementation("net.java.dev.jna:jna-platform:5.11.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.2")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.2")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    implementation("io.ktor:ktor-client-core-jvm:2.1.2")
    implementation("io.ktor:ktor-client-cio:2.1.2")
    implementation("io.ktor:ktor-client-cio-jvm:2.1.2")
    implementation("io.ktor:ktor-client-websockets:2.1.2")
    implementation("io.ktor:ktor-client-auth-jvm:2.1.2")
    implementation("io.ktor:ktor-client-auth:2.1.2")
    implementation("commons-io:commons-io:2.11.0")
    implementation("cn.hutool:hutool-all:5.8.11")
    implementation("io.ktor:ktor-http:2.2.3")
    implementation("com.github.gotson:sqlite-jdbc:3.32.3.8")
    implementation("io.ktor:ktor-client-serialization:2.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.ktor:ktor-server-websockets-jvm:2.0.2")
    implementation("org.jetbrains.pty4j:pty4j:0.12.10")
    implementation("org.apache.groovy:groovy:4.0.10")
    implementation("io.socket:socket.io-client:2.1.0")
}

tasks{
    shadowJar{
        dependencies{
            exclude(dependency("com.github.OhMyMinecraftServer:omms-central:master-SNAPSHOT"))
            exclude(dependency("io.ktor:ktor-server-auth:2.0.2"))
            exclude(dependency("io.ktor:ktor-server-auth-jvm:2.0.2"))
            exclude(dependency("uk.org.lidalia:sysout-over-slf4j:1.0.2"))
            exclude(dependency("org.jline:jline:3.21.0"))
            exclude(dependency("com.mojang:brigadier:1.0.18"))
            exclude(dependency("io.ktor:ktor-server-call-logging-jvm:2.0.2"))
            exclude(dependency("io.ktor:ktor-server-content-negotiation-jvm:2.0.2"))
            exclude(dependency("io.ktor:ktor-server-core-jvm:2.0.2"))
            exclude(dependency("io.ktor:ktor-serialization-kotlinx-json-jvm:2.0.2"))
            exclude(dependency("io.ktor:ktor-serialization-gson-jvm:2.0.2"))
            exclude(dependency("io.ktor:ktor-server-netty-jvm:2.0.2"))
            exclude(dependency("com.google.code.gson:gson:2.9.0"))
            exclude(dependency("org.jetbrains:annotations:23.0.0"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10"))
            exclude(dependency("io.netty:netty-all:4.1.77.Final"))
            exclude(dependency("com.github.oshi:oshi-core:6.1.6"))
            exclude(dependency("net.java.dev.jna:jna:5.11.0"))
            exclude(dependency("net.java.dev.jna:jna-platform:5.11.0"))
            exclude(dependency("io.ktor:ktor-serialization-kotlinx-json:2.0.2"))
            exclude(dependency("io.ktor:ktor-server-content-negotiation:2.0.2"))
            exclude(dependency("me.xdrop:fuzzywuzzy:1.4.0"))
            exclude(dependency("io.ktor:ktor-client-core-jvm:2.1.2"))
            exclude(dependency("io.ktor:ktor-client-cio:2.1.2"))
            exclude(dependency("io.ktor:ktor-client-cio-jvm:2.1.2"))
            exclude(dependency("io.ktor:ktor-client-websockets:2.1.2"))
            exclude(dependency("io.ktor:ktor-client-auth-jvm:2.1.2"))
            exclude(dependency("io.ktor:ktor-client-auth:2.1.2"))
            exclude(dependency("commons-io:commons-io:2.11.0"))
            exclude(dependency("cn.hutool:hutool-all:5.8.11"))
            exclude(dependency("io.ktor:ktor-http:2.2.3"))
            exclude(dependency("com.github.gotson:sqlite-jdbc:3.32.3.8"))
            exclude(dependency("io.ktor:ktor-client-serialization:2.2.3"))
            exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"))
            exclude(dependency("io.ktor:ktor-server-websockets-jvm:2.0.2"))
            exclude(dependency("org.jetbrains.pty4j:pty4j:0.12.10"))
            exclude(dependency("org.apache.groovy:groovy:4.0.10"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}