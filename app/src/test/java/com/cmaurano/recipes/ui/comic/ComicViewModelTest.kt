package com.cmaurano.recipes.ui.comic

import com.cmaurano.recipes.ui.model.Comic
import com.cmaurano.recipes.util.MainCoroutineRule
import com.cmaurano.recipes.util.MockedModelGenerator
import com.cmaurano.recipes.util.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ComicViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val viewModel = ComicViewModel()

    @Test
    fun `successfully retrieve the comics when initialize event is triggered`() {
        mainCoroutineRule.runBlockingTest {
            val expected = listOf<Comic>()
            val observer = viewModel.state.test(this)

            viewModel.onEvent(ComicViewModel.Event.Initialize(MockedModelGenerator.getCharacter()))

            observer.assertValues(ComicViewModel.State.Loading, ComicViewModel.State.Success(expected)).finish()
            observer.assertValueCount(2)
        }
    }
}