package com.frost.elaniinchallenge.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frost.elaniinchallenge.models.PokemonDB

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemones")
    suspend fun getAll(): List<PokemonDB>?

    @Query("SELECT * FROM pokemones WHERE id = :pokemonId")
    suspend fun getPokemon(pokemonId: String): PokemonDB?

    @Query("SELECT * FROM pokemones WHERE region = :region")
    suspend fun getPokemonByRegion(region: String): List<PokemonDB>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(pokemon: PokemonDB)
}