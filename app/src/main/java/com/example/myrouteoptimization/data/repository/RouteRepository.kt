package com.example.myrouteoptimization.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.myrouteoptimization.BuildConfig.GOOGLE_DIRECTION_API_KEY
import com.example.myrouteoptimization.data.source.remote.response.DataItem
import com.example.myrouteoptimization.data.source.remote.response.OptimizeRequest
import com.example.myrouteoptimization.data.source.remote.response.OptimizeResponse
import com.example.myrouteoptimization.data.source.remote.response.RouteResponse
import com.example.myrouteoptimization.data.source.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.myrouteoptimization.utils.Result
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

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

    fun getDetailRoute(id: String, status: String): LiveData<Result<DataItem>> = liveData {
        emit(Result.Loading)

        if (status == "finished") {
            try {
                val response = apiService.getFinishedRoute()
                val data = response.data

                for (i in data.indices) {
                    if (data[i].idResults == id) {
                        emit(Result.Success(data[i]))
                    }
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            }
        } else {
            try {
                val response = apiService.getUnfinishedRoute()
                val data = response.data

                for (i in data.indices) {
                    if (data[i].idResults == id) {
                        emit(Result.Success(data[i]))
                    }
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            }
        }
    }

    fun updateRoute(id: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updateToFinished(id)
            val data = response.message
            emit(Result.Success(data!!))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

     fun optimizeRoute(request : OptimizeRequest) : LiveData<Result<OptimizeResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.optimizeRoute(request)
            emit(Result.Success(response))
        } catch (e : HttpException) {
            val errorRes = e.response()?.errorBody()?.string()
            val parseError = Gson().fromJson(errorRes, OptimizeResponse::class.java)
            emit(Result.Error(parseError.message))
        } catch (e : Exception) {
            emit(Result.Error(e.message ?: "Unexpected Error Occurred"))
        }
    }

    fun getRoute(origin: LatLng, destination: LatLng, waypoints: List<LatLng>): LiveData<Result<List<LatLng>>> = liveData {
        emit(Result.Loading)

        val apiKey = GOOGLE_DIRECTION_API_KEY
        val waypointsString = waypoints.joinToString("|") { "${it.latitude},${it.longitude}" }
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&waypoints=optimize:true|$waypointsString" +
                "&key=$apiKey"

        try {
            val result = withContext(Dispatchers.IO) { URL(url).readText() }
            val jsonResponse = JSONObject(result)
            val routes = jsonResponse.getJSONArray("routes")

            val overviewPolyline = routes.getJSONObject(0)
                .getJSONObject("overview_polyline")
                .getString("points")

            val decodedPath = PolyUtil.decode(overviewPolyline)
            emit(Result.Success(decodedPath))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RouteResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    companion object {
    @Volatile
    private var instance: RouteRepository? = null
    fun getInstance(
        apiService: ApiService
    ): RouteRepository =
        instance ?: synchronized(this) {
            instance ?: RouteRepository(apiService)
        }.also { instance = it }
    }
}