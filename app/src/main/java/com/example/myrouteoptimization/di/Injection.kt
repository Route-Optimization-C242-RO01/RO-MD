package com.example.myrouteoptimization.di

import android.content.Context
import com.example.myrouteoptimization.data.repository.RouteRepository
import com.example.myrouteoptimization.data.repository.UserRepository
import com.example.myrouteoptimization.data.source.datastore.UserPreference
import com.example.myrouteoptimization.data.source.datastore.dataStore
import com.example.myrouteoptimization.data.source.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

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
        return RouteRepository.getInstance(apiService)
    }
}