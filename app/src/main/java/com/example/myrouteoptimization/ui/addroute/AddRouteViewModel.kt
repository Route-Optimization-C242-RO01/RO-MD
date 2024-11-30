package com.example.myrouteoptimization.ui.addroute

import androidx.lifecycle.ViewModel
import com.example.myrouteoptimization.data.repository.RouteRepository
import com.example.myrouteoptimization.data.source.remote.response.OptimizeRequest

class AddRouteViewModel(
    private val routeRepository: RouteRepository
) : ViewModel() {
    fun optimizeRoute(request : OptimizeRequest) = routeRepository.optimizeRoute(request)
}

