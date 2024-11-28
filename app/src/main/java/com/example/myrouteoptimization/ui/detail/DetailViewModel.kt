package com.example.myrouteoptimization.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myrouteoptimization.data.repository.RouteRepository
import com.example.myrouteoptimization.data.repository.UserRepository
import com.example.myrouteoptimization.data.source.datastore.UserModel

class DetailViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getDetailRoute(id: String, status: String) = routeRepository.getDetailRoute(id, status)

    fun updateRoute(id: String) = routeRepository.updateRoute(id)
}