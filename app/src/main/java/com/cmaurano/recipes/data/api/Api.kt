package com.cmaurano.recipes.data.api

import com.cmaurano.recipes.data.model.CharacterResponse
import retrofit2.http.GET

interface Api {

    @GET("characters")
    suspend fun characters(): CharacterResponse
}