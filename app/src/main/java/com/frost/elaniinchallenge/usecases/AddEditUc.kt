package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.repositories.ApiRepository
import com.frost.elaniinchallenge.repositories.DatabaseRepository
import com.frost.elaniinchallenge.repositories.FirebaseRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class AddEditUc @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val firebaseRepository: FirebaseRepository,
    private val crashlyticsInstance: FirebaseCrashlytics) {

    suspend fun buildPokemonList(region: String): ResponseData {
        val result = databaseRepository.getPokemonByRegion(region)
        result.errorMessage?.let {
            crashlyticsInstance.recordException(Throwable(message = it))
            return result
        }
        return ResponseData(pokemonList = result.pokemonDBList?.map { it.mapToPokemon() } as ArrayList<Pokemon>)
    }

    suspend fun saveTeam(team: Team) = firebaseRepository.saveTeam(team)

}