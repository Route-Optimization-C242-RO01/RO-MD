package com.example.myrouteoptimization.data.source.remote.retrofit

import com.example.myrouteoptimization.data.source.remote.response.LoginResponse
import com.example.myrouteoptimization.data.source.remote.response.RegisterResponse
import com.example.myrouteoptimization.data.source.remote.response.RouteResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @GET("unfinished")
    suspend fun getUnfinishedRoute(): RouteResponse

    @GET("finished")
    suspend fun getFinishedRoute(): RouteResponse

    @PUT("/updatetofinished/{id_results}")
    suspend fun updateToFinished(
        @Path("id_results") idResults: String
    ): RouteResponse
}