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
        pokedex = translateTypes(this.types),
        types = this.types?.split(";")?.toTypedArray()?.toList(),
        isSelected = this.isSelected,
        region = this.region
    )

    private fun translateTypes(types: String?): List<String>{
        val finalList = arrayListOf<String>()
        val typeList = types?.split(";")
        typeList?.forEach { finalList.add(getTranslation(it)) }
        return finalList
    }

    private fun getTranslation(type: String) =
        when(type){
            "grass" -> "Hierba"
            "fire" -> "Fuego"
            "poison" -> "Veneno"
            "psychic" -> "Psiquico"
            "fighting" -> "Peleador"
            "water" -> "Agua"
            "normal" -> "Normal"
            "dark" -> "Oscuro"
            "flying" -> "Volador"
            "electric" -> "Electrico"
            "metal" -> "Metal"
            else -> "Unknown"
        }
}
