package com.example.myrouteoptimization.ui.main.todo

import androidx.lifecycle.ViewModel
import com.example.myrouteoptimization.data.repository.RouteRepository

class TodoViewModel(private val routeRepository: RouteRepository) : ViewModel() {
    fun getUnfinishedRoute() = routeRepository.getUnfinishedRoute()
}