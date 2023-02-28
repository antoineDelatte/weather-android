package com.example.weather.repository

import com.example.weather.ResourceError
import com.example.weather.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

abstract class BaseHelper() {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Resource<T>): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                apiToBeCalled()
            } catch (e: HttpException) {
                Resource.Error(errorCode = ResourceError.GLOBAL_ERROR)
            } catch (e: IOException) {
                Resource.Error(errorCode = ResourceError.INTERNET_ERROR)
            } catch (e: Exception) {
                Resource.Error(errorCode = ResourceError.GLOBAL_ERROR)
            }
        }
    }
}