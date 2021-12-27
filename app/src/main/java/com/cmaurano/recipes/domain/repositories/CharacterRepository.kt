package com.cmaurano.recipes.domain.repositories

import com.cmaurano.recipes.domain.model.CharacterDomain

interface CharacterRepository {
    suspend operator fun invoke(): List<CharacterDomain>
}