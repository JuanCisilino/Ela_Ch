package com.frost.elaniinchallenge.repositories

import com.frost.elaniinchallenge.network.PokeApi
import com.frost.elaniinchallenge.Region
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

}