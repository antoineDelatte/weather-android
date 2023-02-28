package com.example.weather.repository.retrofit

import android.util.Log
import com.example.weather.ResourceError
import com.example.weather.model.Location
import com.example.weather.model.Resource
import com.example.weather.repository.BaseHelper
import com.example.weather.repository.bindingModel.GeocodingBindingModel
import com.example.weather.repository.mapper.toLocation
import retrofit2.Response
import javax.inject.Inject

class GeocodingHelper @Inject constructor(
    private val geocodingService: GeocodingService): BaseHelper(), IGeocodingHelper {

    override suspend fun getLocationsByName(locationName: String): Resource<List<Location>> {
        return safeApiCall {
            val response: Response<List<GeocodingBindingModel>> =
                geocodingService.getLocationsByName(locationName)
            handleResponse(response = response)
        }
    }

    override suspend fun getLocationsByCoordinates(
        latitude: Double,
        longitude: Double
    ): Resource<List<Location>> {

        return safeApiCall {
            val response: Response<List<GeocodingBindingModel>> =
                geocodingService.getLocationsByCoordinates(latitude, longitude)
            handleResponse(response)
        }
    }

    private fun handleResponse(response: Response<List<GeocodingBindingModel>>): Resource<List<Location>> {
        val locations: MutableList<Location>
        var location: Location

        if (response.isSuccessful) {
            response.body()?.let {
                locations = ArrayList()
                it.forEach { geocodingBindingModel ->
                    location = geocodingBindingModel.toLocation()
                    locations.add(location)
                }
                return@handleResponse Resource.Success(locations)
            }
            return Resource.Error(errorCode = ResourceError.NOT_FOUND)
        }
        return Resource.Error(errorCode = ResourceError.GLOBAL_ERROR)
    }

}