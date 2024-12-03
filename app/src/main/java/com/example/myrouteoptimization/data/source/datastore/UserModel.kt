package com.example.myrouteoptimization.data.source.datastore

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false
) : Parcelable