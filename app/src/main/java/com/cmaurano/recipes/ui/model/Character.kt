package com.cmaurano.recipes.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val comics: List<Comic>
) : Parcelable

@Parcelize
data class Comic(
    val name: String
) : Parcelable