package com.frost.elaniinchallenge.repositories

import com.frost.elaniinchallenge.database.PokemonDao
import com.frost.elaniinchallenge.models.PokemonDB
import com.frost.elaniinchallenge.models.ResponseData
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val pokemonDao: PokemonDao){

    suspend fun getAllPokemon(): ResponseData {
        val response = pokemonDao.getAll()
        response?:run { return ResponseData(errorMessage = "Error from db") }
        return ResponseData(pokemonDBList = response as ArrayList<PokemonDB>)
    }

    suspend fun getPokemonByRegion(region: String): ResponseData{
        val response = pokemonDao.getPokemonByRegion(region)
        response?:run { return ResponseData(errorMessage = "Error from db") }
        return ResponseData(pokemonDBList = response as ArrayList<PokemonDB>)
    }

    suspend fun insertPokemon(pokemonDB: PokemonDB) {
        pokemonDao.insertUser(pokemonDB)
    }

}