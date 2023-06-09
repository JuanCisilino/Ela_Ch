package com.frost.elaniinchallenge.usecases

import com.frost.elaniinchallenge.repositories.ApiRepository
import com.frost.elaniinchallenge.models.ResponseData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class LoginUC @Inject constructor(
    private val apiRepository: ApiRepository,
    private val crashlyticsInstance: FirebaseCrashlytics,
    private val auth: FirebaseAuth) {

    suspend fun getRegions(): ResponseData {
        val result = apiRepository.getRegions()
        result.errorMessage?.let {
            crashlyticsInstance.recordException(Throwable(message = it))
            return result
        }
        return result
    }

    suspend fun signInWithCredential(credential: AuthCredential) = auth.signInWithCredential(credential)

    suspend fun signOut() = auth.signOut()
}