package com.frost.elaniinchallenge.models

import com.frost.elaniinchallenge.Region

data class ResponseData(
    val errorMessage: String?=null,
    val regionList: ArrayList<Region>?= arrayListOf(),
    val pokemonDBList: ArrayList<PokemonDB>?= arrayListOf(),
    val pokemonList: ArrayList<Pokemon>?= arrayListOf(),
    val teamList: ArrayList<Team>?= arrayListOf(),
    val teamRemoved: Unit?=null
)
