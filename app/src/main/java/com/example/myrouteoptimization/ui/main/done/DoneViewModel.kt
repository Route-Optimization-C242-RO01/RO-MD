package com.example.myrouteoptimization.ui.main.done

import androidx.lifecycle.ViewModel
import com.example.myrouteoptimization.data.repository.RouteRepository

class DoneViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getFinishedRoute() = routeRepository.getFinishedRoute()
}