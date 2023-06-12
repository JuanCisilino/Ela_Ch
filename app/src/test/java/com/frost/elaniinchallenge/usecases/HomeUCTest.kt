package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.repositories.DatabaseRepository
import com.frost.elaniinchallenge.repositories.FirebaseRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HomeUCTest {

    @RelaxedMockK
    private lateinit var firebaseRepository: FirebaseRepository

    @RelaxedMockK
    private lateinit var databaseRepository: DatabaseRepository

    private lateinit var homeUC: HomeUC

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        homeUC = HomeUC(firebaseRepository = firebaseRepository, databaseRepository = databaseRepository)
    }

    @Test
    fun `when the api doesnt return anything then return null`() = runBlocking{
        //Given
        coEvery { firebaseRepository.getTeams() } returns ResponseData(teamReference = null)

        //When
        val response = homeUC.getTeams()

        //Then
        assert(response.teamReference == null)

    }

    @Test
    fun `when the api returns something then return empty list`() = runBlocking{
        val mockedList = arrayListOf<Pokemon>()
        //Given
        coEvery { databaseRepository.getAllPokemon() } returns ResponseData(pokemonList = mockedList)

        //When
        val response = homeUC.getTeamPokemon("1;2;3;4;5;6")

        //Then
        assert(response.pokemonList != null)

    }
}