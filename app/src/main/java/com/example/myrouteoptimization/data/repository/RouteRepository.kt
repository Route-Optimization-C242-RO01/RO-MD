package com.example.myrouteoptimization.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.myrouteoptimization.data.source.remote.response.DataItem
import com.example.myrouteoptimization.data.source.remote.response.OptimizeResponse
import com.example.myrouteoptimization.data.source.remote.response.RouteResponse
import com.example.myrouteoptimization.data.source.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.myrouteoptimization.utils.Result

class RouteRepository (

    private val apiService: ApiService
) {
    fun getUnfinishedRoute(): LiveData<Result<List<DataItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUnfinishedRoute()
            val data = response.data
            emit(Result.Success(data))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    fun getFinishedRoute(): LiveData<Result<List<DataItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedRoute()
            val data = response.data
            emit(Result.Success(data))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    suspend fun optimizeRoute(request : ApiService.OptimizeRequest) : Result<OptimizeResponse> {
        return try {
            val response = apiService.optimizeRoute(request)
            Result.Success(response)
        } catch (e : HttpException) {
            Result.Error(e.message() ?: "Network Exception occurred")
        } catch (e : Exception) {
            Result.Error(e.message ?: "Unexpected Error Occurred")
        }
    }

    companion object {
    @Volatile
    private var instance: RouteRepository? = null
    fun getInstance(
        apiService: ApiService
    ): RouteRepository =
        instance ?: synchronized(this) {
            instance ?: RouteRepository( apiService)
        }.also { instance = it }
    }
}