package com.cmaurano.recipes.ui.comic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmaurano.recipes.ui.model.Character
import com.cmaurano.recipes.ui.model.Comic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComicViewModel : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.Initialize -> getComicsFromFragmentArguments(event.character)
        }
    }

    private fun getComicsFromFragmentArguments(character: Character?) = viewModelScope.launch {
        _state.emit(State.Loading)

        if (character == null) {
            _state.emit(State.Empty)
        } else {
            _state.emit(State.Success(character.comics))
        }
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        object Empty : State()
        data class Success(val comic: List<Comic>) : State()
    }

    sealed class Event {
        data class Initialize(val character: Character?) : Event()
    }
}