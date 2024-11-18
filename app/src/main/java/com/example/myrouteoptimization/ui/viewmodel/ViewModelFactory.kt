package com.example.myrouteoptimization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myrouteoptimization.ui.main.todo.TodoViewModel
import kotlin.jvm.Throws

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoViewModel::class.java) -> {
                TodoViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel ${modelClass.name}")
        }
    }


    companion object {
        private const val TAG = "ViewModelFactory"
    }
}