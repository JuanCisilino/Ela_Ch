package com.frost.elaniinchallenge.models

import androidx.recyclerview.widget.DiffUtil

data class Pokemon(
    var id: String?=null,
    var name: String?=null,
    var image: String?=null,
    var pokedex: List<String>?= listOf(),
    var types: List<String>?= listOf(),
    var isSelected: Boolean?=false,
    var region: String?=null
): DiffUtil.ItemCallback<Pokemon>() {

    fun mapToPokemonDB() = PokemonDB(
        id = this.id?:"0",
        name = this.name,
        image = this.image,
        pokedex = this.pokedex?.joinToString(separator = ";"),
        types = this.types?.joinToString(separator = ";"),
        isSelected = this.isSelected,
        region = this.region
    )

    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }
}
