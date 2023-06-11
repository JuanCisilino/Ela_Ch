package com.frost.elaniinchallenge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.usecases.HomeUC
import com.frost.elaniinchallenge.usecases.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUC: HomeUC):ViewModel() {

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
        viewModelScope.launch {
            val result = homeUC.getCreated(email)
        }
    }
}