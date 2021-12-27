package com.cmaurano.recipes.ui.comic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cmaurano.recipes.databinding.FragmentCharacterComicsBinding
import com.cmaurano.recipes.ui.comic.ComicViewModel.Event
import com.cmaurano.recipes.ui.comic.ComicViewModel.State
import com.cmaurano.recipes.ui.model.Character
import com.cmaurano.recipes.ui.model.Comic
import com.cmaurano.recipes.ui.post.CharactersViewModel.Companion.RECIPE_BUNDLE_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ComicFragment : Fragment() {

    private lateinit var binding: FragmentCharacterComicsBinding
    private val viewModel: ComicViewModel by viewModels()
    private val comicAdapter = ComicAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharacterComicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObservers()

        if (savedInstanceState == null) {
            val character = arguments?.getParcelable<Character>(RECIPE_BUNDLE_KEY)
            viewModel.onEvent(Event.Initialize(character))
        }
    }

    override fun onDestroy() {
        findNavController().popBackStack()
        super.onDestroy()
    }

    private fun setUpRecyclerView() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = comicAdapter
        }
    }

    private fun setUpObservers() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect { state ->
            when (state) {
                is State.Error -> setVisibility(isErrorShown = true)
                is State.Loading -> setVisibility(isLoadingShown = true)
                is State.Empty -> setVisibility(isEmptyShown = true)
                is State.Success -> handleContent(state.comic)
            }
        }
    }

    private fun setVisibility(
        isErrorShown: Boolean = false,
        isLoadingShown: Boolean = false,
        isContentShown: Boolean = false,
        isEmptyShown: Boolean = false
    ) {
        with(binding) {
            recyclerView.isVisible = isContentShown
            loading.isVisible = isLoadingShown
            error.isVisible = isErrorShown
            empty.isVisible = isEmptyShown
        }
    }

    private fun handleContent(comics: List<Comic>) {
        setVisibility(isContentShown = true)
        comicAdapter.submitList(comics)
    }
}