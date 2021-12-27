package com.cmaurano.recipes.ui.post

import com.cmaurano.recipes.domain.GetCharactersUseCase
import com.cmaurano.recipes.util.MainCoroutineRule
import com.cmaurano.recipes.util.MockedModelGenerator
import com.cmaurano.recipes.util.test
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class CharactersViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getCharactersUseCase = mockk<GetCharactersUseCase>()
    private val viewModel = CharactersViewModel(getCharactersUseCase)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `successfully retrieve the characters when initialize event is triggered`() {
        mainCoroutineRule.runBlockingTest {
            val useCaseResponse = listOf(MockedModelGenerator.getCharacterDomain())

            coEvery { getCharactersUseCase() } returns useCaseResponse

            val expected = listOf(MockedModelGenerator.getCharacter())
            val observer = viewModel.state.test(this)

            viewModel.onEvent(CharactersViewModel.Event.Initialize)

            observer.assertValues(CharactersViewModel.State.Loading, CharactersViewModel.State.Success(expected)).finish()
            observer.assertValueCount(2)
        }
    }
}