package com.example.myrouteoptimization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myrouteoptimization.ui.main.done.DoneViewModel
import com.example.myrouteoptimization.ui.main.profile.ProfileViewModel
import com.example.myrouteoptimization.ui.main.todo.TodoViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoViewModel::class.java) -> {
                TodoViewModel() as T
            }
            modelClass.isAssignableFrom(DoneViewModel::class.java) -> {
                DoneViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel ${modelClass.name}")
        }
    }
}