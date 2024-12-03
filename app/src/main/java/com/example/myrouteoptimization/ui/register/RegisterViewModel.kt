package com.example.myrouteoptimization.ui.register

import androidx.lifecycle.ViewModel
import com.example.myrouteoptimization.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, pass: String, retypePass: String) = userRepository.register(name, pass, retypePass)
}