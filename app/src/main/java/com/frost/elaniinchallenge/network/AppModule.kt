package com.frost.elaniinchallenge.network

import com.frost.elaniinchallenge.repositories.ApiRepository
import com.frost.elaniinchallenge.repositories.FirebaseRepository
import com.frost.elaniinchallenge.usecases.LoginUC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDbInstance() = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseRTDatabase(dbInstance: FirebaseDatabase) = FirebaseRepository(dbInstance)

    @Singleton
    @Provides
    fun provideCrashlyticsInstance() = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideRetrofit(): PokeApi {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApiRepository(pokeApi: PokeApi) = ApiRepository(pokeApi)

    @Singleton
    @Provides
    fun provideLoginUC(
        apiRepository: ApiRepository,
        crashlyticsInstance: FirebaseCrashlytics,
        authInstance: FirebaseAuth) =
        LoginUC(apiRepository, crashlyticsInstance, authInstance)

}