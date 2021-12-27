package com.cmaurano.recipes.domain

import com.cmaurano.recipes.data.api.Api
import com.cmaurano.recipes.util.MainCoroutineRule
import com.cmaurano.recipes.util.MockedModelGenerator
import com.cmaurano.recipes.util.shouldBe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
internal class GetCharactersUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val characterRepository = CharacterRepositoryFake()
    private val getCharactersUseCase = GetCharactersUseCase(characterRepository)

    @Test
    fun `return a list of characters per user successfully`() {
        val expected = listOf(MockedModelGenerator.getCharacterDomain())

        mainCoroutineRule.runBlockingTest {
            val actual = getCharactersUseCase.invoke()

            expected shouldBe actual
        }
    }

}