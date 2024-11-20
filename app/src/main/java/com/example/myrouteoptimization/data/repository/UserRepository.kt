package com.example.myrouteoptimization.data.repository

class UserRepository {


    companion object {
        @Volatile
        private var INSTANCE : UserRepository? = null

        @JvmStatic
        fun getInstance() : UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository()
            }
    }
}