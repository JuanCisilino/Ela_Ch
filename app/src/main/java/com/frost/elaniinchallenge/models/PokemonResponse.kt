package com.frost.elaniinchallenge.models

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val results: ArrayList<PokeNameResult>
)

data class PokeNameResult(
    val name: String
)

data class PokemonId(
    val id: Int,
    val sprites: Sprite,
    val types: ArrayList<Type>
)

data class Type(
    val type: Tipo
)

data class Tipo(
    val name: String
)

data class Sprite(
    val other: ArtWork
)

data class ArtWork(
    @SerializedName("official-artwork")
    val artwork: Official
)

data class Official(
    val front_default: String
)

data class PokemonSpecie(
    val flavor_text_entries: ArrayList<Flavor>,
    val generation: Generation
)

data class Flavor(
    val flavor_text: String,
    val language: Language
)

data class Generation(
    val name: String
)

data class Language(
    val name: String
)