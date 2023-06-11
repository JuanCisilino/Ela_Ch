package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.repositories.ApiRepository
import com.frost.elaniinchallenge.repositories.DatabaseRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class AddEditUc @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val apiRepository: ApiRepository,
    private val crashlyticsInstance: FirebaseCrashlytics) {

    suspend fun buildPokemonList(region: String): ResponseData {
        val result = databaseRepository.getPokemonByRegion(region)
        result.errorMessage?.let {
            crashlyticsInstance.recordException(Throwable(message = it))
            return result
        }
        return ResponseData(pokemonList = result.pokemonDBList?.map { it.mapToPokemon() } as ArrayList<Pokemon>)
    }
}