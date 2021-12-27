package com.cmaurano.recipes.di

import com.cmaurano.recipes.data.api.Api
import com.cmaurano.recipes.data.repositories.CharacterRepositoryImpl
import com.cmaurano.recipes.domain.repositories.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCharacterRepository(service: Api): CharacterRepository = CharacterRepositoryImpl(service)
}