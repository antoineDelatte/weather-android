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
            val locations: MutableList<Location>
            var location: Location

            val response: Response<List<GeocodingBindingModel>> =
                geocodingService.getLocationsByName(locationName)
            if (response.isSuccessful) {
                response.body()?.let {
                    locations = ArrayList()
                    it.forEach { geocodingBindingModel ->
                        location = geocodingBindingModel.toLocation()
                        locations.add(location)
                    }
                    return@safeApiCall Resource.Success(locations)
                }
                return@safeApiCall Resource.Error(errorCode = ResourceError.NOT_FOUND)
            }
            Resource.Error(errorCode = ResourceError.GLOBAL_ERROR)
        }
    }

    override suspend fun getLocationByCoordinates(
        latitude: Double,
        longitude: Double
    ): Resource<Location> {
        return safeApiCall {
            var location: Location

            val response: Response<GeocodingBindingModel> =
                geocodingService.getLocationsByCoordinates(latitude, longitude)
            if (response.isSuccessful) {
                response.body()?.let {
                    location = it.toLocation()
                    return@safeApiCall Resource.Success(location)
                }
                return@safeApiCall Resource.Error(errorCode = ResourceError.NOT_FOUND)
            }
            Resource.Error(errorCode = ResourceError.GLOBAL_ERROR)
        }
    }

}