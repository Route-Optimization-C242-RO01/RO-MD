package com.example.myrouteoptimization.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrouteoptimization.data.repository.UserRepository
import com.example.myrouteoptimization.data.source.datastore.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    suspend fun login(name: String, pass: String) = userRepository.login(name, pass)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}