package com.example.myrouteoptimization.di

import com.example.myrouteoptimization.data.repository.RouteRepository
import com.example.myrouteoptimization.data.repository.UserRepository

/**
 * A singleton object that provides dependency injection for various repositories.
 *
 * The `Injection` object contains methods to provide instances of different repositories
 * used within the application. These methods are typically used to inject dependencies
 * into various parts of the application, such as ViewModels or other components, to
 * manage the data layer efficiently.
 *
 * This approach helps in decoupling the components and makes the application more testable
 * and maintainable.
 */
object Injection {
    /**
     * Provides an instance of the UserRepository.
     *
     * This function provides the UserRepository that is responsible for handling
     * user-related data operations, such as fetching user data, saving user information,
     * and managing user preferences. This repository is typically used in the ViewModel
     * or other components that require user data.
     *
     * @return An instance of UserRepository.
     */
    fun provideUserRepository() : UserRepository {
        return UserRepository.getInstance()
    }

    /**
     * Provides an instance of the RouteRepository.
     *
     * This function provides the RouteRepository that is responsible for handling
     * route-related data operations, such as fetching route data, saving routes, or
     * managing route-related preferences. This repository is typically used in ViewModels
     * or other components that need to work with route data.
     *
     * @return An instance of RouteRepository.
     */
    fun provideRouteRepository() : RouteRepository {
        return RouteRepository.getInstance()
    }
}