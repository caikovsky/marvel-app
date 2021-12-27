package com.cmaurano.recipes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val code: Int,
    val etag: String,
    val data: CharacterResultsResponse,
)

@Serializable
data class CharacterResultsResponse(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<CharacterListResponse>
)

@Serializable
data class CharacterListResponse(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: CharacterThumbnailResponse,
    val comics: CharacterComicResponse,
)

@Serializable
data class CharacterComicResponse(
    val items: List<CharacterComicItemsResponse>
)

@Serializable
data class CharacterComicItemsResponse(
    val name: String
)

@Serializable
data class CharacterThumbnailResponse(
    val path: String,
    val extension: String
)


