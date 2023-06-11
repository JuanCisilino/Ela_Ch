package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.repositories.ApiRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class HomeUC @Inject constructor(
    private val auth: FirebaseAuth,
    private val apiRepository: ApiRepository,
) {

    suspend fun signOut() = auth.signOut()

    suspend fun getCreated(email: String){

    }
}