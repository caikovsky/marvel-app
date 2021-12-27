package com.cmaurano.recipes.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cmaurano.recipes.R
import com.cmaurano.recipes.databinding.CharactersFragmentBinding
import com.cmaurano.recipes.ui.model.Character
import com.cmaurano.recipes.ui.post.CharactersViewModel.Event
import com.cmaurano.recipes.ui.post.CharactersViewModel.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private lateinit var binding: CharactersFragmentBinding
    private val viewModel: CharactersViewModel by viewModels()
    private val postAdapter = CharacterAdapter { character -> onCharacterClicked(character) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setListeners()
        setUpObservers()

        if (savedInstanceState == null) viewModel.onEvent(Event.Initialize)
    }

    private fun setListeners() {
        with(binding) {
            swipeLayout.setOnRefreshListener {
                binding.swipeLayout.isRefreshing = false
            }
        }
    }

    private fun setUpObservers() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        launch {
            viewModel.state.collect { state ->
                when (state) {
                    is State.Error -> setVisibility(isErrorShown = true)
                    is State.Loading -> setVisibility(isLoadingShown = true)
                    is State.Success -> handleContent(state.character)
                }
            }
        }

        launch {
            viewModel.action.collect { action ->
                when (action) {
                    is CharactersViewModel.Action.CharacterSelected -> handleCharacterSelected(action.bundle)
                }
            }
        }
    }

    private fun onCharacterClicked(character: Character) {
        viewModel.onEvent(Event.SelectCharacter(character))
    }

    private fun handleCharacterSelected(bundle: Bundle) {
        findNavController().navigate(R.id.action_characterFragment_to_comicFragment, bundle)
    }

    private fun handleContent(characters: List<Character>) {
        setVisibility(isContentShown = true)
        postAdapter.submitList(characters)
    }

    private fun setUpRecyclerView() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = postAdapter
        }
    }

    private fun setVisibility(
        isContentShown: Boolean = false,
        isErrorShown: Boolean = false,
        isLoadingShown: Boolean = false,
    ) {
        with(binding) {
            recyclerView.isVisible = isContentShown
            loading.isVisible = isLoadingShown
            error.isVisible = isErrorShown
        }
    }
}