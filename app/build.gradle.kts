val javaVersion = JavaVersion.VERSION_17
val ktorVersion = "2.1.1"
val kotlinxSerializationVersion = "1.4.0"
val graphqlKotlinVersion = "5.3.1"
val kotestVersion = "5.4.2"
val mockkVersion = "1.12.8"

plugins {
    kotlin("plugin.serialization") version "1.7.10"
    application
}

dependencies {
    implementation(project(":azureAuth"))
    // detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib"))
    implementation("com.github.navikt:rapids-and-rivers:2022082414021661342533.46a423f6c163")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    implementation("ch.qos.logback:logback-classic:1.4.1")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")

    implementation("org.jetbrains:annotations:23.0.0")
    // Ktor client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:$kotlinxSerializationVersion")
    // Arrow
    implementation("io.arrow-kt:arrow-core:1.1.2")

    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.2.5")
    testImplementation("io.kotest:kotest-extensions:$kotestVersion")
    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-dsl-jvm:$mockkVersion")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
}

application {
    mainClass.set("no.nav.tiltakspenger.fakta.person.ApplicationKt")
}
