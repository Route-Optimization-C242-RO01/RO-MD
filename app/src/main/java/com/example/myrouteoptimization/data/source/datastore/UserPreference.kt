package com.example.myrouteoptimization.data.source.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "session_data_token")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){


    companion object {
        @Volatile
        private var INSTANCE : UserPreference? = null

        fun getInstance(
            dataStore: DataStore<Preferences>
        ) : UserPreference {
            return INSTANCE ?: synchronized(this) {
                val singletonInstance = UserPreference(dataStore)
                INSTANCE = singletonInstance
                singletonInstance
            }
        }
    }
}