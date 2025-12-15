package com.example.shops.domain.common

sealed class RequestResource<T> {

    class Loading<T> : RequestResource<T>()

    data class Success<T>(val data: T) : RequestResource<T>()

    data class Error<T>(val failure: DataFailure) : RequestResource<T>()
}