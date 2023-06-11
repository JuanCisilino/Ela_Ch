package com.frost.elaniinchallenge.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemones")
data class PokemonDB(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String?=null,
    @ColumnInfo(name = "image") var image: String?=null,
    @ColumnInfo(name = "region") var region: String?=null,
    @ColumnInfo(name = "pokedex") var pokedex: String?=null,
    @ColumnInfo(name = "types") var types: String?=null,
    @ColumnInfo(name = "isSelected") var isSelected: Boolean?=false
) {

    fun mapToPokemon() = Pokemon(
        id = this.id,
        name = this.name,
        image = this.image,
        pokedex = this.pokedex?.split(";")?.toTypedArray()?.toList(),
        types = this.types?.split(";")?.toTypedArray()?.toList(),
        isSelected = this.isSelected,
        region = this.region
    )
}
