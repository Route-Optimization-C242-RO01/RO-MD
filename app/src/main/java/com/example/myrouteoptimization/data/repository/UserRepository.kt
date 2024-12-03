package com.example.myrouteoptimization.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.myrouteoptimization.data.source.datastore.UserModel
import com.example.myrouteoptimization.data.source.datastore.UserPreference
import com.example.myrouteoptimization.data.source.remote.response.LoginResponse
import com.example.myrouteoptimization.data.source.remote.response.LoginResult
import com.example.myrouteoptimization.data.source.remote.response.RegisterResponse
import com.example.myrouteoptimization.data.source.remote.retrofit.ApiService
import retrofit2.HttpException
import com.example.myrouteoptimization.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

     fun register(name: String, email: String, password: String): LiveData<Result<String?>> = liveData  {
        emit(Result.Loading)
        try {
            val message = apiService.register(name, email, password).message
            emit(Result.Success(message))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

     fun login(name: String, password: String): LiveData<Result<LoginResult>> = liveData  {
        emit(Result.Loading)
        try {
            val result = apiService.login(name, password)
            val username = result.name
            val token = result.token
            emit(Result.Success(LoginResult(username, token)))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}