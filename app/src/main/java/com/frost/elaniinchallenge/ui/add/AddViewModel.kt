package com.frost.elaniinchallenge.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.usecases.AddUc
import com.frost.elaniinchallenge.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val addEditUC: AddUc): ViewModel() {

    var loadStateLiveData = MutableLiveData<LoadState>()
    var pokemonListLiveData = MutableLiveData<List<Pokemon>>()
    var errorLiveData = MutableLiveData<String>()
    var teamLiveData = MutableLiveData<Unit>()
    var saveLiveData = MutableLiveData<Unit>()

    private var selectedList = arrayListOf<Pokemon>()
    private var partialList = arrayListOf<Pokemon>()
    lateinit var email: String
    private set
    lateinit var region: String
    private set


    fun setPair(pair: Pair<String, String>){
        region = pair.first
        email = pair.second
        onCreate()
    }

    private fun onCreate(){
        loadStateLiveData.postValue(LoadState.Loading)
        viewModelScope.launch {
            val result = addEditUC.buildPokemonList(region)

            result.errorMessage?.let { errorLiveData.postValue(it) }
            result.pokemonList
                ?.let {
                    partialList = it
                    pokemonListLiveData.postValue(it)
                    loadStateLiveData.postValue(LoadState.Success)
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

    fun addToSelected(pokemon: Pokemon) {
        if (selectedList.size < 7){
            selectedList.add(pokemon)
            updatePartialList(pokemon)
            checkIfValid()
        }
    }

    private fun updatePartialList(pokemon: Pokemon) {
        partialList.remove(pokemon)
        partialList.add(pokemon)
        pokemonListLiveData.postValue(partialList)
    }

    fun checkIfValid() {
        if (selectedList.size > 2) teamLiveData.postValue(Unit)
    }

    fun removeFromSelected(pokemon: Pokemon) {
        selectedList.remove(pokemon)
        updatePartialList(pokemon)
        checkIfValid()
    }

    fun save(teamName: String){
        loadStateLiveData.postValue(LoadState.Loading)
        val idList = selectedList.map { it.id }
        viewModelScope.launch {
            val team = Team(
                id = generateUUID(),
                email = email,
                region = region,
                name = teamName,
                pokemonIds = idList.joinToString(";")
            )
            addEditUC.saveTeam(team)
            loadStateLiveData.postValue(LoadState.Success)
            saveLiveData.postValue(Unit)
        }
    }

    private fun generateUUID() = kotlin.math.abs((100000..999999).random())

}