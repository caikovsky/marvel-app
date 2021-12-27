package com.cmaurano.recipes.domain.model

data class CharacterDomain(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val comics: List<ComicDomain>
)

data class ComicDomain(
    val name: String,
)