package com.cmaurano.recipes.ui.post

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmaurano.recipes.domain.GetCharactersUseCase
import com.cmaurano.recipes.domain.model.CharacterDomain
import com.cmaurano.recipes.domain.model.ComicDomain
import com.cmaurano.recipes.ui.model.Character
import com.cmaurano.recipes.ui.model.Comic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val getCharactersUseCase: GetCharactersUseCase) : ViewModel() {

    companion object {
        const val RECIPE_BUNDLE_KEY = "RECIPE_BUNDLE_KEY"
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _action = MutableStateFlow<Action>(Action.None)
    val action = _action.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.Initialize, Event.Refresh -> getPostsPerUser()
            is Event.SelectCharacter -> selectCharacter(event.character)
        }
    }

    private fun selectCharacter(character: Character) = viewModelScope.launch {
        val bundle = bundleOf(RECIPE_BUNDLE_KEY to character)
        _action.emit(Action.CharacterSelected(bundle))
    }

    private fun getPostsPerUser() = viewModelScope.launch {
        _state.emit(State.Loading)

        runCatching { getCharactersUseCase() }
            .onSuccess { character ->
                _state.emit(State.Success(character.toViewEntities()))
            }.onFailure { throwable ->
                Log.e(this::class.simpleName, "throw -> ${throwable.message}")
                _state.emit(State.Error)
            }
    }

    private fun List<CharacterDomain>.toViewEntities(): List<Character> = map { domain ->
        Character(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            thumbnail = domain.thumbnail,
            comics = domain.comics.toViewEntities()
        )
    }

    @JvmName("toViewEntitiesComicDomain")
    private fun List<ComicDomain>.toViewEntities(): List<Comic> = map { domain ->
        Comic(name = domain.name)
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        data class Success(val character: List<Character>) : State()
    }

    sealed class Action {
        object None : Action()
        data class CharacterSelected(val bundle: Bundle) : Action()
    }

    sealed class Event {
        object Initialize : Event()
        object Refresh : Event()
        data class SelectCharacter(val character: Character) : Event()
    }
}
