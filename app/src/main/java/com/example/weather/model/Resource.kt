package com.example.weather.model

import com.example.weather.ResourceError


sealed class Resource<T>(
    val data: T? = null,
    val errorCode: ResourceError? = null
) {


    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(errorCode: ResourceError) : Resource<T>(errorCode = errorCode)

    class Loading<T> : Resource<T>()
}
