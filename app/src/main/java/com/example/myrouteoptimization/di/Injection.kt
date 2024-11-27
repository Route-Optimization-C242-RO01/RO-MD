package com.example.myrouteoptimization.di

import android.content.Context
import com.example.myrouteoptimization.data.repository.RouteRepository
import com.example.myrouteoptimization.data.repository.UserRepository
import com.example.myrouteoptimization.data.source.datastore.UserPreference
import com.example.myrouteoptimization.data.source.datastore.dataStore
import com.example.myrouteoptimization.data.source.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * A singleton object that provides dependency injection for various repositories.
 *
 * The `Injection` object contains methods to provide instances of different repositories
 * used within the application. These methods are typically used to inject dependencies
 * into various parts of the application, such as ViewModels or other components, to
 * manage the data layer efficiently.
 *
 * This approach helps in decoupling the components and makes the application more testable
 * and maintainable.
 */
object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideRouteRepository(context: Context): RouteRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return RouteRepository.getInstance(pref, apiService)
    }
}