package com.frost.elaniinchallenge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frost.elaniinchallenge.models.PokemonDB

@Database(entities = [PokemonDB::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao

}