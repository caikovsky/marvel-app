package com.cmaurano.recipes.ui.comic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cmaurano.recipes.databinding.ItemComicBinding
import com.cmaurano.recipes.ui.model.Comic

class ComicAdapter : ListAdapter<Comic, ComicAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListViewHolder(
        private val itemBinding: ItemComicBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(comic: Comic) {
            itemBinding.run {
                comicName.text = comic.name
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comic>() {
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem == newItem
            }
        }
    }
}
