package com.frost.elaniinchallenge.network

import com.frost.elaniinchallenge.RegionResponse
import retrofit2.Response
import retrofit2.http.GET

interface PokeApi {

    @GET("region/")
    suspend fun getRegions(): Response<RegionResponse>
}