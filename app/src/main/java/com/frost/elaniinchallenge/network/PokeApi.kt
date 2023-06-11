package com.frost.elaniinchallenge.network

import com.frost.elaniinchallenge.RegionResponse
import com.frost.elaniinchallenge.models.PokemonId
import com.frost.elaniinchallenge.models.PokemonResponse
import com.frost.elaniinchallenge.models.PokemonSpecie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    @GET("region/")
    suspend fun getRegions(): Response<RegionResponse>

    @GET("pokemon/?offset=0&limit=1009")
    suspend fun getAllPokemon(): Response<PokemonResponse>

    @GET("pokemon/{id}/")
    suspend fun getById(@Path("id") id: String): Response<PokemonId>

    @GET("pokemon-species/{id}/")
    suspend fun getBySpecie(@Path("id") id: String): Response<PokemonSpecie>
}