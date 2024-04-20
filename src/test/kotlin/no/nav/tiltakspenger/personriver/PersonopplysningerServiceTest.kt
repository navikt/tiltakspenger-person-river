package no.nav.tiltakspenger.personriver

import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import no.nav.helse.rapids_rivers.testsupport.TestRapid
import no.nav.tiltakspenger.libs.person.AdressebeskyttelseGradering
import no.nav.tiltakspenger.libs.person.BarnIFolkeregisteret
import no.nav.tiltakspenger.libs.person.Person
import no.nav.tiltakspenger.libs.person.PersonRespons
import no.nav.tiltakspenger.personriver.person.PersonClient
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import java.time.LocalDate
import java.time.Month

class PersonopplysningerServiceTest {
    private fun mockRapid(): Pair<TestRapid, PersonClient> {
        val personResponse = PersonRespons(
            person = Person(
                barn = listOf(
                    BarnIFolkeregisteret(
                        ident = "ident",
                        fornavn = "test",
                        etternavn = "testesen",
                        mellomnavn = null,
                        fødselsdato = LocalDate.of(2022, Month.JUNE, 21),
                        adressebeskyttelseGradering = AdressebeskyttelseGradering.UGRADERT,
                    ),
                ),
                barnUtenFolkeregisteridentifikator = emptyList(),
                fødselsdato = LocalDate.of(2020, Month.APRIL, 10),
                fornavn = "test",
                mellomnavn = null,
                etternavn = "testesen",
                adressebeskyttelseGradering = AdressebeskyttelseGradering.UGRADERT,
                gtLand = "NOR",
                gtKommune = null,
                gtBydel = null,
            ),
            feil = null,
        )

        val rapid = TestRapid()
        val personClient = mockk<PersonClient>()
        PersonopplysningerService(
            rapidsConnection = rapid,
            personClient = personClient,
        )
        coEvery { personClient.hentPerson(any()) } returns personResponse
        return Pair(rapid, personClient)
    }

    @Test
    fun `skal svare på person-behov`() {
        val (rapid, personClient) = mockRapid()
        val ident = "121212132323"
        // language=JSON
        rapid.sendTestMessage(
            """
            { 
              "@behov": ["personopplysninger"], 
              "ident": "$ident", 
              "@id": "1", 
              "@behovId": "2"
            }
            """.trimIndent(),
        )
        coVerify { personClient.hentPerson(ident) }

        println(rapid.inspektør.message(0).toString())
        // language=JSON
        JSONAssert.assertEquals(
            """
            {"@løsning": {
              "personopplysninger": {
                "person": {
                    "fornavn": "test",
                    "etternavn":  "testesen",
                    "mellomnavn": null,
                    "fødselsdato": "2020-04-10",
                    "adressebeskyttelseGradering": "UGRADERT",
                    "gtLand": "NOR",
                    "gtKommune": null,
                    "gtBydel": null,
                    "barn": [{
                      "fornavn": "test",
                      "etternavn": "testesen",
                      "mellomnavn": null,
                      "fødselsdato": "2022-06-21"
                    }]
                },
                "feil": null
              }
            }
            }
            """.trimIndent(),
            rapid.inspektør.message(0).toString(),
            JSONCompareMode.LENIENT,
        )
    }

    @Test
    fun `skal ikke svare på person-behov som er løst`() {
        val (rapid, personClient) = mockRapid()
        val ident = "121212132323"
        // language=JSON
        rapid.sendTestMessage(
            """
            { 
                "@behov": ["personopplysninger"], 
                "ident": "$ident", 
                "@id": "1", 
                "@behovId": "2", 
                "@løsning": "hei"
            }
            """.trimIndent(),
        )
        coVerify { personClient.hentPerson(any()) wasNot Called }
    }
}
