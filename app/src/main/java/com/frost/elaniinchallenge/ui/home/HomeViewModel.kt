package com.frost.elaniinchallenge.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.usecases.HomeUC
import com.frost.elaniinchallenge.usecases.LoginUC
import com.frost.elaniinchallenge.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUC: HomeUC):ViewModel() {

    var teamsLiveData = MutableLiveData<List<Team>>()
    var loadStateLiveData = MutableLiveData<LoadState>()
    var errorLiveData = MutableLiveData<String>()

    private var teamList = arrayListOf<Team>()
    private lateinit var email: String
    private lateinit var region: String

    fun setValues(pair: Pair<String, String>){
        region = pair.first
        email = pair.second
    }

    fun signOut(){
        viewModelScope.launch {
            homeUC.signOut()
        }
    }

    fun getCreated(){
        loadStateLiveData.postValue(LoadState.Loading)
        viewModelScope.launch {
            val result = homeUC.getCreated(email)

            result.errorMessage?.let { errorLiveData.postValue(it) }
            result.teamList
                ?.let {
                    teamsLiveData.postValue(it)
                    teamList = it
                    loadStateLiveData.postValue(LoadState.Success)
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

    fun removeTeam(team: Team){
        loadStateLiveData.postValue(LoadState.Loading)
        val index = teamList.indexOf(team)
        viewModelScope.launch {
            val result = homeUC.removeTeam(index)

            result.errorMessage?.let { errorLiveData.postValue(it) }
            result.teamRemoved
                ?.let {
                    teamList.remove(team)
                    teamsLiveData.postValue(teamList)
                    loadStateLiveData.postValue(LoadState.Success)
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

}