package com.frost.elaniinchallenge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.elaniinchallenge.usecases.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val loginUC: LoginUC):ViewModel() {

    fun signOut(){
        viewModelScope.launch {
            loginUC.signOut()
        }
    }
}