package com.frost.elaniinchallenge.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.usecases.HomeUC
import com.frost.elaniinchallenge.utils.LoadState
import com.google.firebase.database.DatabaseReference
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    @RelaxedMockK
    private lateinit var homeUC: HomeUC

    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(homeUC)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
    }

    @Test
    fun `when search returns null validate live data value as Loading`() = runTest {
        //Given
        coEvery { homeUC.getTeams() } returns ResponseData(teamReference = null)

        //When
        viewModel.setValues(Pair("alola", "test@test.com"))

        //Then
        assert(viewModel.loadStateLiveData.value == LoadState.Loading)
    }

    @Test
    fun `when search returns null validate live data state value as null`() = runTest {
        //Given
        coEvery { homeUC.removeTeam(1) } returns Unit

        //When
        viewModel.removeTeam(Team())

        //Then
        assert(viewModel.loadStateLiveData.value == LoadState.Success)
    }

}