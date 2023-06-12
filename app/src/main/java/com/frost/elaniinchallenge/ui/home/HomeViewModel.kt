package com.frost.elaniinchallenge.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.usecases.HomeUC
import com.frost.elaniinchallenge.usecases.LoginUC
import com.frost.elaniinchallenge.utils.LoadState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUC: HomeUC):ViewModel() {

    var teamsLiveData = MutableLiveData<List<Team>>()
    var loadStateLiveData = MutableLiveData<LoadState>()
    var errorLiveData = MutableLiveData<String>()
    var selectedTeamLiveData = MutableLiveData<Pair<String, List<Pokemon>>>()
    var saveLiveData = MutableLiveData<Unit>()

    private var teamList = arrayListOf<Team>()
    private var entireList = arrayListOf<Team>()
    private lateinit var email: String
    private lateinit var region: String
    var selectedTeam: Team ?= null
    private set


    fun setValues(pair: Pair<String, String>){
        region = pair.first
        email = pair.second
        getCreated()
    }

    fun signOut(){
        viewModelScope.launch {
            homeUC.signOut()
        }
    }

    private fun getCreated(){
        loadStateLiveData.postValue(LoadState.Loading)
        viewModelScope.launch {
            val result = homeUC.getCreated()
            result.teamReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    teamList.clear()
                    dataSnapshot.children.forEach { data ->
                        data.getValue(Team::class.java)?.let { teamList.add(it) }
                    }
                    entireList = teamList
                    teamList = teamList.filter { it.email == email } as ArrayList<Team>
                    teamsLiveData.postValue(teamList.filter { it.region == region })
                    loadStateLiveData.postValue(LoadState.Success)
                }

                override fun onCancelled(error: DatabaseError) {
                    errorLiveData.postValue("Error from db")
                }
            })
        }
    }

    fun removeTeam(team: Team){
        loadStateLiveData.postValue(LoadState.Loading)
        val index = teamList.indexOf(team)
        viewModelScope.launch {
            val result = homeUC.removeTeam(team.id!!)

            val teamRef = result.instance?.getReference("teams/${index}")
            teamRef?.removeValue()
                ?.addOnSuccessListener {
                    teamList.remove(team)
                    teamsLiveData.postValue(teamList)
                    loadStateLiveData.postValue(LoadState.Success)
                }
                ?.addOnFailureListener { error ->
                    errorLiveData.postValue(error.message)
                    loadStateLiveData.postValue(LoadState.Error)
                }
        }
    }

    fun createTeamToShow(team: Team){
        viewModelScope.launch {
            val result = team.pokemonIds?.let { homeUC.getTeamPokemon(it) }

            result?.errorMessage?.let { errorLiveData.postValue(it) }
            result?.pokemonList
                ?.let {
                    selectedTeam = team
                    selectedTeamLiveData.postValue(Pair(team.name!!, it))
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

    fun save(teamId: String){
        loadStateLiveData.postValue(LoadState.Loading)
        val externalTeam = entireList.find { it.id == teamId.toInt() }
        viewModelScope.launch {
            val team = Team(
                id = generateUUID(),
                email = email,
                region = region,
                name = externalTeam?.name,
                pokemonIds = externalTeam?.pokemonIds
            )
            homeUC.saveTeam(team)
            loadStateLiveData.postValue(LoadState.Success)
            saveLiveData.postValue(Unit)
        }
    }

    private fun generateUUID() = kotlin.math.abs((100000..999999).random())

}