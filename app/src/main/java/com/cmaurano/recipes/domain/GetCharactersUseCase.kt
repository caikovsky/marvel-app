package com.cmaurano.recipes.domain

import com.cmaurano.recipes.domain.model.CharacterDomain
import com.cmaurano.recipes.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(): List<CharacterDomain> {
        val characters = characterRepository()

        return characters.map { character ->
            CharacterDomain(
                id = character.id,
                name = character.name,
                description = character.description,
                thumbnail = character.thumbnail,
                comics = character.comics
            )
        }
    }
}





