package com.example.myrouteoptimization.data.source.datastore

data class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)