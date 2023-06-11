package com.frost.elaniinchallenge.ui.addedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.usecases.AddEditUc
import com.frost.elaniinchallenge.usecases.LoginUC
import com.frost.elaniinchallenge.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val addEditUC: AddEditUc): ViewModel() {

    var loadStateLiveData = MutableLiveData<LoadState>()
    var pokemonListLiveData = MutableLiveData<List<Pokemon>>()
    var errorLiveData = MutableLiveData<String>()

    lateinit var selectedList: ArrayList<Pokemon>
    private set
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
                    pokemonListLiveData.postValue(it)
                    loadStateLiveData.postValue(LoadState.Success)
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

}