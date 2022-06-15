package no.nav.tiltakspenger.fakta.person.pdl

import arrow.core.*
import arrow.core.continuations.either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.tiltakspenger.fakta.person.Configuration.getPDLUrl
import no.nav.tiltakspenger.azureAuth.azureClient
import no.nav.tiltakspenger.azureAuth.OauthConfig
import no.nav.tiltakspenger.fakta.person.Configuration
import no.nav.tiltakspenger.fakta.person.pdl.models.Person
import no.nav.tiltakspenger.fakta.person.pdl.models.avklarFødsel
import no.nav.tiltakspenger.fakta.person.pdl.models.avklarNavn

val url = getPDLUrl()
const val INDIVIDSTONAD = "IND"

sealed class PDLClientError {
    object FantIkkePerson : PDLClientError()
    object NavnKunneIkkeAvklares: PDLClientError()
    data class NetworkError(val exception: Throwable): PDLClientError()
    data class UkjentFeil(val errors: List<PdlError>): PDLClientError()
}

class PDLClient(val client: HttpClient = azureClient(
    OauthConfig.fromEnv(
        scope = Configuration.getPdlScope(),
    )
)) {
    private suspend fun fetchPerson(ident: String): Either<PDLClientError, HentPersonResponse> {
        kotlin.runCatching {
            return@runCatching client.post(url) {
                accept(ContentType.Application.Json)
                header("Tema", INDIVIDSTONAD)
                contentType(ContentType.Application.Json)
                setBody(hentPersonQuery(ident))
            }.body<HentPersonResponse>()
        }
            .fold(
                { return it.right() },
                { return PDLClientError.NetworkError(it).left() },
            )
    }

    suspend fun hentPerson(ident: String): Either<PDLClientError, Person> {
        return either {
            val response = fetchPerson(ident).bind()
            val person = response.getPerson().bind()
            Person(
                fødsel = avklarFødsel(person.foedsel),
                navn = avklarNavn(person.navn).bind()
            )
        }
    }
}

