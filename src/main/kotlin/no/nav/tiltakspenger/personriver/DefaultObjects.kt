package no.nav.tiltakspenger.personriver

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson.jackson
import mu.KotlinLogging
import java.time.Duration

private val LOG = KotlinLogging.logger {}
private val SECURELOG = KotlinLogging.logger("tjenestekall")
private const val SIXTY_SECONDS = 60L

fun httpClientCIO() = HttpClient(CIO).medDefaultConfig()
fun httpClientGeneric(engine: HttpClientEngine) = HttpClient(engine).medDefaultConfig()

private fun HttpClient.medDefaultConfig() = this.config {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            setDefaultPrettyPrinter(
                DefaultPrettyPrinter().apply {
                    indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                    indentObjectsWith(DefaultIndenter("  ", "\n"))
                },
            )
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    install(HttpTimeout) {
        connectTimeoutMillis = Duration.ofSeconds(SIXTY_SECONDS).toMillis()
        requestTimeoutMillis = Duration.ofSeconds(SIXTY_SECONDS).toMillis()
        socketTimeoutMillis = Duration.ofSeconds(SIXTY_SECONDS).toMillis()
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                LOG.info("HttpClient detaljer logget til securelog")
                SECURELOG.info(message)
            }
        }
        level = LogLevel.ALL
    }
    expectSuccess = true
}
