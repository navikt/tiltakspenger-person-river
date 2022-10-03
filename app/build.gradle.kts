val javaVersion = JavaVersion.VERSION_17
val ktorVersion = "2.1.2"
val kotlinxSerializationVersion = "1.4.0"
val graphqlKotlinVersion = "5.3.1"
val kotestVersion = "5.5.0"
val jacksonVersion = "2.13.4"
val mockkVersion = "1.13.2"

plugins {
    application
}

dependencies {
    // detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib"))
    implementation("com.github.navikt:rapids-and-rivers:2022092314391663936769.9d5d33074875")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    implementation("ch.qos.logback:logback-classic:1.4.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")

    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("io.arrow-kt:arrow-core:1.1.3")
    
    // Ktor client
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jvm:$ktorVersion")
    implementation("io.ktor:ktor-utils-jvm:$ktorVersion")
    implementation("io.ktor:ktor-http-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")


    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")

    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.2.5")
    testImplementation("io.kotest:kotest-extensions:$kotestVersion")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-dsl-jvm:$mockkVersion")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
}

application {
    mainClass.set("no.nav.tiltakspenger.fakta.person.ApplicationKt")
}
