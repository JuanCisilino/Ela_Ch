package com.frost.elaniinchallenge.repositories

import com.frost.elaniinchallenge.network.PokeApi
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import javax.inject.Inject

class ApiRepository @Inject constructor(private val pokeApi: PokeApi) {

    suspend fun getRegions(): ResponseData {
        val response = pokeApi.getRegions()
        response.errorBody()?.let { return ResponseData(errorMessage = it.toString()) }
        val list = response.body()?.results as ArrayList<Region>
        val toRemove = list.find { it.name == "hisui" }
        list.remove(toRemove)
        return ResponseData(regionList = list)
    }

    suspend fun generatePokemonList(): ResponseData {
        val response = pokeApi.getAllPokemon()
        response.errorBody()?.let { return ResponseData(errorMessage = it.toString()) }
        val nameList = response.body()?.results?:return ResponseData(errorMessage = "null result")
        val pokemonList = arrayListOf<Pokemon>()
        var index = 1
        nameList.forEach {
            val pokemon = Pokemon()
            pokemon.name = it.name
            pokemon.id = index.toString()
            val specieResponse = pokeApi.getBySpecie(pokemon.id!!)
            index += 1
            specieResponse.errorBody()?.let { return ResponseData(errorMessage = it.toString()) }
            val pokeGeneration = specieResponse.body()?:return ResponseData(errorMessage = "null result")
            pokemon.region = turnGenerationToNumber(pokeGeneration.generation.name)
            val flavors = pokeGeneration.flavor_text_entries.filter { it.language.name == "es" }
            pokemon.pokedex = flavors.map { it.flavor_text } as ArrayList<String>
            val idResponse = pokeApi.getById(pokemon.id!!)
            idResponse.errorBody()?.let { return ResponseData(errorMessage = it.toString()) }
            val pokeResponse = idResponse.body()?:return ResponseData(errorMessage = "null result")
            pokemon.image = pokeResponse.sprites.other.artwork.front_default
            pokemon.types = pokeResponse.types.map { it.type.name } as ArrayList<String>
            pokemonList.add(pokemon)
        }
        return ResponseData(pokemonList = pokemonList)
    }

    private fun turnGenerationToNumber(generation: String) =
        when(generation){
            "generation-ix" -> "paldea"
            "generation-viii" -> "galar"
            "generation-vii" -> "alola"
            "generation-vi" -> "kalos"
            "generation-v" -> "unova"
            "generation-iv" -> "sinnoh"
            "generation-iii" -> "hoenn"
            "generation-ii" -> "johto"
            else -> "kanto"
        }

}