package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.repositories.ApiRepository
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.repositories.DatabaseRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class LoginUC @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val apiRepository: ApiRepository,
    private val crashlyticsInstance: FirebaseCrashlytics,
    private val auth: FirebaseAuth) {

    suspend fun getRegions(): ResponseData {
        val result = apiRepository.getRegions()
        result.errorMessage?.let {
            crashlyticsInstance.recordException(Throwable(message = it))
            return result
        }
        return result
    }

    suspend fun initialRun(): ResponseData {
        val list = databaseRepository.getAllPokemon()
        return if (list.pokemonDBList.isNullOrEmpty()) {
            ResponseData(pokemonList = null)
        } else {
            ResponseData(pokemonList = list.pokemonDBList.map { it.mapToPokemon() } as ArrayList<Pokemon>)
        }
    }

    suspend fun createDBPokemonList(){
        val pokemonBack = apiRepository.generatePokemonList()
        pokemonBack.pokemonList?.forEach { databaseRepository.insertPokemon(it.mapToPokemonDB()) }
    }

    suspend fun signInWithCredential(credential: AuthCredential) = auth.signInWithCredential(credential)

    suspend fun signOut() = auth.signOut()
}