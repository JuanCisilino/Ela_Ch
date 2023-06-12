package com.frost.elaniinchallenge.models

import com.frost.elaniinchallenge.Region
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class ResponseData(
    val errorMessage: String?=null,
    val regionList: ArrayList<Region>?= arrayListOf(),
    val pokemonDBList: ArrayList<PokemonDB>?= arrayListOf(),
    val pokemonList: ArrayList<Pokemon>?= arrayListOf(),
    val teamList: ArrayList<Team>?= arrayListOf(),
    val teamRemoved: Unit?=null,
    val teamReference: DatabaseReference?=null,
    val instance: FirebaseDatabase?=null
)
