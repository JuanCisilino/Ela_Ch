package com.frost.elaniinchallenge.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.usecases.LoginUC
import com.frost.elaniinchallenge.utils.LoadState
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val loginUC: LoginUC): ViewModel() {

    var loadStateLiveData = MutableLiveData<LoadState>()
    var regionListLiveData = MutableLiveData<List<Region>>()
    var databaseLiveData = MutableLiveData<Unit?>()
    var loginLiveData = MutableLiveData<Unit>()
    var errorLiveData = MutableLiveData<String>()

    private lateinit var regionList: List<Region>
    lateinit var mail: String
    private set

    fun onCreate(){
        viewModelScope.launch {
            val result = loginUC.getRegions()

            result.errorMessage?.let { errorLiveData.postValue(it) }
            result.regionList
                ?.let {
                    regionList = it
                    regionListLiveData.postValue(it)
                    loadStateLiveData.postValue(LoadState.Success)
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

    fun initDatabase(){
        viewModelScope.launch {
            val result = loginUC.initialRun()
            result.pokemonList
                ?.let { databaseLiveData.postValue(Unit) }
                ?:run {
                    databaseLiveData.postValue(null)
                    loginUC.createDBPokemonList()
                }
        }
    }

    fun signIn(credential: AuthCredential, email: String){
        viewModelScope.launch {
            mail = email
            loginUC.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful){
                    loginLiveData.postValue(Unit)
                } else {
                    loginLiveData.postValue(Unit)
                    errorLiveData.postValue(it.exception?.message)
                }
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            loginUC.signOut()
        }
    }
}