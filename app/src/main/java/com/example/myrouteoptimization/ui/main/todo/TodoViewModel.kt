package com.example.myrouteoptimization.ui.main.todo

import androidx.lifecycle.ViewModel
import com.example.myrouteoptimization.data.repository.RouteRepository
import com.google.android.gms.maps.model.LatLng

class TodoViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getUnfinishedRoute() = routeRepository.getUnfinishedRoute()

    fun getRoute(origin: LatLng, destination: LatLng, waypoints: List<LatLng>) =
        routeRepository.getRoute(origin, destination, waypoints)
}