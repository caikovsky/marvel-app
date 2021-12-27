package com.cmaurano.recipes.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.cmaurano.recipes.databinding.ItemCharacterBinding
import com.cmaurano.recipes.ui.model.Character

class CharacterAdapter(private val onClickListener: (Character) -> Unit) : ListAdapter<Character, CharacterAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding, onClickListener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListViewHolder(
        private val itemBinding: ItemCharacterBinding,
        private val onClickListener: (Character) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(character: Character) {
            itemBinding.run {
                characterName.text = character.name
                characterThumbnail.load(character.thumbnail) { transformations(CircleCropTransformation()) }
                itemView.setOnClickListener { onClickListener(character) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }
}