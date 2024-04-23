package no.nav.tiltakspenger.personriver

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.personriver.auth.AzureTokenProvider
import no.nav.tiltakspenger.personriver.pdl.PDLClient
import no.nav.tiltakspenger.personriver.pdl.PDLService

fun main() {
    System.setProperty("logback.configurationFile", "egenLogback.xml")
    val log = KotlinLogging.logger {}
    val securelog = KotlinLogging.logger("tjenestekall")

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error { "Uncaught exception logget i securelog" }
        securelog.error(e) { e.message }
    }
    val tokenProvider = AzureTokenProvider()
    log.info { "Starting tiltakspenger-person" }
    RapidApplication.create(no.nav.tiltakspenger.personriver.Configuration.rapidsAndRivers)
        .apply {
            no.nav.tiltakspenger.personriver.PersonopplysningerService(
                rapidsConnection = this,
                pdlService = PDLService(pdlClient = PDLClient(getToken = tokenProvider::getToken)),
            )

            register(object : RapidsConnection.StatusListener {
                override fun onStartup(rapidsConnection: RapidsConnection) {
                    log.info { "Starting tiltakspenger-person" }
                }

                override fun onShutdown(rapidsConnection: RapidsConnection) {
                    log.info { "Stopping tiltakspenger-person" }
                    super.onShutdown(rapidsConnection)
                }
            })
        }.start()
}