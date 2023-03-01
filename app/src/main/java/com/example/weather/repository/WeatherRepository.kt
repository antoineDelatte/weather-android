package com.example.weather.repository

import com.example.weather.ResourceError
import com.example.weather.model.Location
import com.example.weather.model.WeatherForecast
import com.example.weather.model.Resource
import com.example.weather.repository.retrofit.IGeocodingHelper
import com.example.weather.repository.retrofit.IWeatherHelper
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherHelper: IWeatherHelper,
    private val geocodingHelper: IGeocodingHelper
) {

    suspend fun getForecastByCoordinates(latitude: Double, longitude: Double) : Resource<WeatherForecast> {
        val location: Location

        val weatherForecastResource: Resource<WeatherForecast> =
            weatherHelper.getForecastByCoordinates(latitude, longitude)
        val locationResource: Resource<Location> =
            geocodingHelper.getLocationByCoordinates(latitude, longitude)
        if (weatherForecastResource.data != null) {
            location = locationResource.data ?: Location(latitude, longitude)
            weatherForecastResource.data.location = location
        }
        return weatherForecastResource
    }

    suspend fun getForecastByLocationName(locationName: String): Resource<WeatherForecast> {
        val locationsResource: Resource<List<Location>>
        var location: Location?
        val weatherForecastResource: Resource<WeatherForecast>

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
                weatherForecastResource.data.location = location
            }
            return weatherForecastResource
        } else if (locationsResource.errorCode != null) {
            return Resource.Error(locationsResource.errorCode);
        } else {
            return Resource.Error(ResourceError.NOT_FOUND)
        }
    }
}