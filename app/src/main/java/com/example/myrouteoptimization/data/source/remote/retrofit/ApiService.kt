package com.example.myrouteoptimization.data.source.remote.retrofit

import com.example.myrouteoptimization.data.source.remote.response.LoginResponse
import com.example.myrouteoptimization.data.source.remote.response.OptimizeRequest
import com.example.myrouteoptimization.data.source.remote.response.OptimizeResponse
import com.example.myrouteoptimization.data.source.remote.response.RegisterResponse
import com.example.myrouteoptimization.data.source.remote.response.RouteResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    /**
     * Fungsi untuk melakukan registrasi pengguna baru.
     * @param name Nama pengguna yang ingin didaftarkan.
     * @param password Kata sandi pengguna yang ingin didaftarkan.
     * @param retypePass Konfirmasi kata sandi pengguna.
     * @return [RegisterResponse] Menyediakan respon hasil registrasi.
     */
    @FormUrlEncoded
    @POST("user")
    suspend fun register(
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("retype_pass") retypePass: String
    ): RegisterResponse

    /**
     * Fungsi untuk melakukan login pengguna.
     * @param name Nama pengguna untuk login.
     * @param password Kata sandi pengguna untuk login.
     * @return [LoginResponse] Menyediakan respon hasil login, termasuk token autentikasi jika berhasil.
     */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("name") name: String,
        @Field("password") password: String
    ): LoginResponse

    /**
     * Fungsi untuk mengoptimalkan rute pengiriman.
     * @param request [OptimizeRequest] Data yang digunakan untuk proses optimasi rute.
     * @return [OptimizeResponse] Respon hasil optimasi rute, berisi rute yang sudah dioptimalkan.
     */
    @POST("optimize")
    suspend fun optimizeRoute(
        @Body request: OptimizeRequest
    ) : OptimizeResponse

    /**
     * Fungsi untuk mengambil data rute yang belum selesai.
     * @return [RouteResponse] Respon yang berisi daftar rute yang belum selesai diproses.
     */
    @GET("unfinished")
    suspend fun getUnfinishedRoute(): RouteResponse

    /**
     * Fungsi untuk mengambil data rute yang sudah selesai.
     * @return [RouteResponse] Respon yang berisi daftar rute yang telah selesai diproses.
     */
    @GET("finished")
    suspend fun getFinishedRoute(): RouteResponse

    /**
     * Fungsi untuk memperbarui status rute menjadi "selesai" setelah selesai dioptimalkan.
     * @param idResults ID rute yang akan diperbarui statusnya menjadi selesai.
     * @return [RouteResponse] Respon yang berisi status terbaru dari rute yang diperbarui.
     */
    @PUT("/updatetofinished/{id_results}")
    suspend fun updateToFinished(
        @Path("id_results") idResults: String
    ): RouteResponse
}
