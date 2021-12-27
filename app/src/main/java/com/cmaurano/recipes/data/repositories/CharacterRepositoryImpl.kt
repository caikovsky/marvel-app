package com.cmaurano.recipes.data.repositories

import com.cmaurano.recipes.data.api.Api
import com.cmaurano.recipes.data.model.CharacterComicResponse
import com.cmaurano.recipes.data.model.CharacterResponse
import com.cmaurano.recipes.domain.model.CharacterDomain
import com.cmaurano.recipes.domain.model.ComicDomain
import com.cmaurano.recipes.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val api: Api) : CharacterRepository {
    override suspend fun invoke() = api.characters().toDomain()

    private fun CharacterResponse.toDomain(): List<CharacterDomain> =
        this
            .data
            .results
            .map {
                CharacterDomain(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    thumbnail = "${it.thumbnail.path}.${it.thumbnail.extension}",
                    comics = it.comics.toDomain()
                )
            }

    private fun CharacterComicResponse.toDomain(): List<ComicDomain> =
        this
            .items
            .map {
                ComicDomain(name = it.name)
            }
}