package com.example.weather.repository

import com.example.weather.ResourceError
import com.example.weather.model.Location
import com.example.weather.model.WeatherForecast
import com.example.weather.repository.retrofit.GeocodingHelper
import com.example.weather.model.Resource
import com.example.weather.repository.retrofit.WeatherHelper
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherHelper: WeatherHelper,
    private val geocodingHelper: GeocodingHelper) {

    suspend fun getForecastByCoordinates(latitude: Double, longitude: Double) : Resource<WeatherForecast> {
        val weatherForecastResource: Resource<WeatherForecast>
        val locationsResource: Resource<List<Location>>
        val location: Location

        weatherForecastResource = weatherHelper.getForecastByCoordinates(latitude, longitude)
        locationsResource = geocodingHelper.getLocationsByCoordinates(latitude, longitude)
        if (weatherForecastResource.data != null) {
            if (locationsResource.data != null && locationsResource.data.isNotEmpty()) {
                location = locationsResource.data.first()
            } else {
                location = Location(latitude, longitude)
            }
            weatherForecastResource.data.location = location
        }
        return weatherForecastResource
    }

    suspend fun getForecastByLocationName(locationName: String): Resource<WeatherForecast> {
        val locationsResource: Resource<List<Location>>
        var location: Location?
        var weatherForecastResource: Resource<WeatherForecast>

        locationsResource = geocodingHelper.getLocationsByName(locationName)
        if (locationsResource.data != null && locationsResource.data.isNotEmpty()) {
            location = locationsResource.data.find { loc ->
                loc.name?.let { locName ->
                    return@find locationName.contains(locName, ignoreCase = true)
                }
                false
            }
            if (location == null) {
                location = locationsResource.data.first()
            }

            weatherForecastResource =
                weatherHelper.getForecastByCoordinates(location.latitude, location.longitude)
            if (weatherForecastResource.data != null) {
                weatherForecastResource.data!!.location = location
            }
            return weatherForecastResource
        } else if (locationsResource.errorCode != null) {
            return Resource.Error(locationsResource.errorCode);
        } else {
            return Resource.Error(ResourceError.NOT_FOUND)
        }
    }
}