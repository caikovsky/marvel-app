package com.cmaurano.recipes.domain

import com.cmaurano.recipes.domain.model.CharacterDomain
import com.cmaurano.recipes.domain.repositories.CharacterRepository
import com.cmaurano.recipes.util.MockedModelGenerator

class CharacterRepositoryFake : CharacterRepository {
    override suspend fun invoke(): List<CharacterDomain> = listOf(MockedModelGenerator.getCharacterDomain())
}
