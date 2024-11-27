package com.example.myrouteoptimization.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.myrouteoptimization.data.source.datastore.UserPreference
import com.example.myrouteoptimization.data.source.remote.response.DataItem
import com.example.myrouteoptimization.data.source.remote.response.RouteResponse
import com.example.myrouteoptimization.data.source.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.myrouteoptimization.utils.Result

class RouteRepository (
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun getUnfinishedRoute(): LiveData<Result<List<DataItem>>> = liveData {
        emit(Result.Loading)
//        val response = apiService.getUnfinishedRoute()

//        if (response.code == 200) {
//            val data = response.data
//            emit(Result.Success(data))
//        } else {
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
//            val errorMessage = errorBody.message
//            emit(Result.Error())
//        }


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

//    fun getStory(): LiveData<Result<List<ListStoryItem>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getStory()
//            val stories = response.listStory
//            emit(Result.Success(stories))
//        } catch (e: HttpException) {
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
//            val errorMessage = errorBody.message
//            emit(Result.Error(errorMessage!!))
//        }
//    }

    companion object {
    @Volatile
    private var instance: RouteRepository? = null
    fun getInstance(
        userPreference: UserPreference,
        apiService: ApiService
    ): RouteRepository =
        instance ?: synchronized(this) {
            instance ?: RouteRepository(userPreference, apiService)
        }.also { instance = it }
    }
}