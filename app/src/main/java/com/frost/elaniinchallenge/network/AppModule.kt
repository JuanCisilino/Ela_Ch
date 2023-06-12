package com.frost.elaniinchallenge.network

import android.content.Context
import androidx.room.Room
import com.frost.elaniinchallenge.database.Database
import com.frost.elaniinchallenge.database.PokemonDao
import com.frost.elaniinchallenge.repositories.ApiRepository
import com.frost.elaniinchallenge.repositories.DatabaseRepository
import com.frost.elaniinchallenge.repositories.FirebaseRepository
import com.frost.elaniinchallenge.usecases.AddUc
import com.frost.elaniinchallenge.usecases.HomeUC
import com.frost.elaniinchallenge.usecases.LoginUC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATABASE = "pokemon_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, Database::class.java, DATABASE).build()

    @Singleton
    @Provides
    fun providePokemonDao(db: Database) = db.getPokemonDao()

    @Singleton
    @Provides
    fun provideDatabaseRepository(pokemonDao: PokemonDao) = DatabaseRepository(pokemonDao)

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
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS) // Tiempo límite de conexión de 40 segundos
            .readTimeout(120, TimeUnit.SECONDS) // Tiempo límite de lectura de 40 segundos
            .build()
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(okHttpClient)
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
        databaseRepository: DatabaseRepository,
        apiRepository: ApiRepository,
        crashlyticsInstance: FirebaseCrashlytics,
        authInstance: FirebaseAuth) =
        LoginUC(databaseRepository, apiRepository, crashlyticsInstance, authInstance)

    @Singleton
    @Provides
    fun provideHomeUC(
        authInstance: FirebaseAuth,
        firebaseRepository: FirebaseRepository,
        databaseRepository: DatabaseRepository) = HomeUC(authInstance, firebaseRepository, databaseRepository)

    @Singleton
    @Provides
    fun provideAddEditUC(
        databaseRepository: DatabaseRepository,
        firebaseRepository: FirebaseRepository,
        crashlyticsInstance: FirebaseCrashlytics) =
        AddUc(databaseRepository, firebaseRepository, crashlyticsInstance)

}