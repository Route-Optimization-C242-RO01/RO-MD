package com.example.myrouteoptimization.ui.detail

import androidx.lifecycle.ViewModel
import com.example.myrouteoptimization.data.repository.RouteRepository

class DetailViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getDetailRoute(id: String, status: String) = routeRepository.getDetailRoute(id, status)

    fun updateRoute(id: String) = routeRepository.updateRoute(id)
}