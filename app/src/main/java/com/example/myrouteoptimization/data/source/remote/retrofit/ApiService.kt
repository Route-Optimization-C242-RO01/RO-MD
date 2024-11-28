package com.example.myrouteoptimization.data.source.remote.retrofit

import com.example.myrouteoptimization.data.source.remote.response.LoginResponse
import com.example.myrouteoptimization.data.source.remote.response.OptimizeResponse
import com.example.myrouteoptimization.data.source.remote.response.PostDataItem
import com.example.myrouteoptimization.data.source.remote.response.RegisterResponse
import com.example.myrouteoptimization.data.source.remote.response.RouteResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("user")
    suspend fun register(
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("retype_pass") retypePass: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("name") name: String,
        @Field("password") password: String
    ): LoginResponse


    data class OptimizeRequest(
        @SerializedName("Number_of_vehicles") val numberOfVehicles : Int,
        @SerializedName("title") val title: String,
        @SerializedName("status") val status : String,
        @SerializedName("data") val data : List<PostDataItem>
    )

    @POST("optimize")
    suspend fun optimizeRoute(
        @Body request: OptimizeRequest
    ) : OptimizeResponse

    @GET("unfinished")
    suspend fun getUnfinishedRoute(): RouteResponse
}