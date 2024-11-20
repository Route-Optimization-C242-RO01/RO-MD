package com.example.myrouteoptimization.utils

/**
 * A sealed class representing the result of a network or data operation.
 * This class is used to represent the outcome of an operation, which can be either:
 * - a successful result with data (`Success`),
 * - an error with an error message (`Error`), or
 * - an ongoing operation in progress (`Loading`).
 *
 * This class can be used to handle different states of an operation, such as success, failure, and loading,
 * in a safe and structured way.
 *
 * @param R The type of data that may be returned in case of success. This is a generic type.
 */
sealed class Result<out R> private constructor(){
    /**
     * Represents a successful operation with a result of type [T].
     *
     * This class holds the data returned from a successful operation.
     *
     * @param T The type of data returned upon success.
     * @property data The result data from the successful operation.
     */
    data class Success<out T>(val data : T) : Result<T>()
    /**
     * Represents an error that occurred during the operation.
     *
     * This class holds an error message describing what went wrong.
     *
     * @property error A string message describing the error.
     */
    data class Error(val error : String) : Result<Nothing>()
    /**
     * Represents an ongoing operation that is currently loading.
     *
     * This class is used to indicate that an operation is in progress and has not yet completed.
     */
    data object Loading : Result<Nothing>()
}