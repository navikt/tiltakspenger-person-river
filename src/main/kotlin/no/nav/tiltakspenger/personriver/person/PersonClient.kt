package no.nav.tiltakspenger.personriver.person

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import no.nav.tiltakspenger.libs.person.PersonRespons
import no.nav.tiltakspenger.personriver.httpClientCIO
import no.nav.tiltakspenger.personriver.Configuration as Conf

class PersonClient(
    private val personKlientConfig: PersonKlientConfig = Conf.personKlientConfig(),
    private val getToken: suspend () -> String,
    private val httpClient: HttpClient = httpClientCIO(),
) {
    companion object {
        const val navCallIdHeader = "Nav-Call-Id"
    }

    data class RequestBody(
        val ident: String,
    )

    suspend fun hentPerson(ident: String): PersonRespons {
        val httpResponse =
            httpClient.preparePost("${personKlientConfig.baseUrl}/azure/pdl/personalia") {
                header(navCallIdHeader, "tiltakspenger-person-river")
                bearerAuth(getToken())
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                setBody(RequestBody(ident))
            }.execute()

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.call.response.body<PersonRespons>()
            else -> throw RuntimeException("error (responseCode=${httpResponse.status.value}) from tiltakspenger-person")
        }
    }

    data class PersonKlientConfig(
        val baseUrl: String,
    )
}
