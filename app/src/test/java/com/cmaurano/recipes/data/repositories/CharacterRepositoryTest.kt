package com.cmaurano.recipes.data.repositories

import com.cmaurano.recipes.data.api.Api
import com.cmaurano.recipes.di.NetworkModule
import com.cmaurano.recipes.util.MockedModelGenerator
import com.cmaurano.recipes.util.readFile
import com.cmaurano.recipes.util.shouldBe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit

@ExperimentalSerializationApi
internal class CharacterRepositoryTest {

    private val server = MockWebServer()
    private val service = Retrofit.Builder()
        .baseUrl(server.url("/"))
        .addConverterFactory(NetworkModule.jsonSerialization.asConverterFactory("application/json".toMediaType()))
        .client(OkHttpClient.Builder().build())
        .build()
        .create(Api::class.java)

    private val characterRepository = CharacterRepositoryImpl(service)

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `should hit characters endpoint on the API with return a list of characters based on first marvel hero`() = runBlocking {
        server.enqueue(MockResponse().setBody(readFile("characters.json")))

        val actual = characterRepository()[0]
        val expected = MockedModelGenerator.getCharacterDomain(
            id = 1011334,
            name = "3-D Man",
            description = "",
            thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg",
            comics = listOf(
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #14"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #14 (SPOTLIGHT VARIANT)"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #15"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #16"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #17"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #18"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #18 (ZOMBIE VARIANT)"),
                MockedModelGenerator.getComicDomain(name = "Avengers: The Initiative (2007) #19"),
                MockedModelGenerator.getComicDomain(name = "Deadpool (1997) #44"),
                MockedModelGenerator.getComicDomain(name = "Marvel Premiere (1972) #35"),
                MockedModelGenerator.getComicDomain(name = "Marvel Premiere (1972) #36"),
                MockedModelGenerator.getComicDomain(name = "Marvel Premiere (1972) #37"),
            )
        )


        expected shouldBe actual
    }
}