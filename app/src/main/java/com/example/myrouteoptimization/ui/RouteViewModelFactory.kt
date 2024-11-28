package com.example.myrouteoptimization.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myrouteoptimization.data.repository.RouteRepository
import com.example.myrouteoptimization.di.Injection
import com.example.myrouteoptimization.ui.detail.DetailViewModel
import com.example.myrouteoptimization.ui.main.done.DoneViewModel
import com.example.myrouteoptimization.ui.main.todo.TodoViewModel

class RouteViewModelFactory(private val routeRepository: RouteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoViewModel::class.java) -> {
                TodoViewModel(routeRepository) as T
            }
            modelClass.isAssignableFrom(DoneViewModel::class.java) -> {
                DoneViewModel(routeRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(routeRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RouteViewModelFactory? = null
        @JvmStatic
        fun getInstanceRoute(context: Context): RouteViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RouteViewModelFactory::class.java) {
                    INSTANCE = RouteViewModelFactory(Injection.provideRouteRepository(context))
                }
            }
            return INSTANCE as RouteViewModelFactory
        }
    }
}