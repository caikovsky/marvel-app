package com.cmaurano.recipes.util

import com.cmaurano.recipes.data.model.CharacterListResponse
import com.cmaurano.recipes.data.model.CharacterResponse
import com.cmaurano.recipes.data.model.CharacterResultsResponse
import com.cmaurano.recipes.domain.model.CharacterDomain
import com.cmaurano.recipes.domain.model.ComicDomain
import com.cmaurano.recipes.ui.model.Character
import com.cmaurano.recipes.ui.model.Comic

object MockedModelGenerator {

    fun getCharacterResponse(
        code: Int = 200,
        results: List<CharacterListResponse> = listOf(),
    ) = CharacterResponse(
        code = code,
        etag = "",
        data = CharacterResultsResponse(
            offset = 0,
            limit = 0,
            total = 0,
            count = 0,
            results = results,
        )
    )

    fun getCharacterDomain(
        id: Int = 1,
        name: String = "",
        description: String = "",
        thumbnail: String = "",
        comics: List<ComicDomain> = listOf(),
    ) = CharacterDomain(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
        comics = comics
    )

    fun getComicDomain(name: String = ""): ComicDomain = ComicDomain(name = name)


    fun getCharacter(
        id: Int = 1,
        name: String = "",
        description: String = "",
        thumbnail: String = "",
        comics: List<Comic> = listOf(),
    ) = Character(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
        comics = comics
    )

    fun getComic(name: String = ""): Comic = Comic(name = name)

}
