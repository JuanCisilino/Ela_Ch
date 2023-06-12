package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.repositories.DatabaseRepository
import com.frost.elaniinchallenge.repositories.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class HomeUC @Inject constructor(
    private val auth: FirebaseAuth?=null,
    private val firebaseRepository: FirebaseRepository,
    private val databaseRepository: DatabaseRepository,
    private val crashlyticsInstance: FirebaseCrashlytics?=null
) {

    suspend fun signOut() = auth?.signOut()

    suspend fun getTeams() = firebaseRepository.getTeams()

    suspend fun removeTeam(teamId: Int) = firebaseRepository.removeTeam(teamId)

    suspend fun getTeamPokemon(teamList: String): ResponseData {
        val list = databaseRepository.getAllPokemon()
        val pokeList = arrayListOf<Pokemon>()
        val pokemons = teamList.split(";")
        pokemons.forEach { id ->
            val pokemon = list.pokemonDBList?.find { it.id == id }
            pokemon?.let { pokeList.add(it.mapToPokemon()) }
                ?:run {
                    crashlyticsInstance?.recordException(Throwable(message = "No se pudo crear pokemon de equipo"))
                    return ResponseData(errorMessage = "No se pudo crear pokemon de equipo") }
        }
        return ResponseData(pokemonList = pokeList)
    }

    suspend fun saveTeam(team: Team) = firebaseRepository.saveTeam(team)
}