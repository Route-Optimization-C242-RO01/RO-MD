package com.example.myrouteoptimization.data.repository

class RouteRepository {

    companion object {
        @Volatile
        private var INSTANCE : RouteRepository? = null

        @JvmStatic
        fun getInstance() : RouteRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RouteRepository()
            }
    }
}