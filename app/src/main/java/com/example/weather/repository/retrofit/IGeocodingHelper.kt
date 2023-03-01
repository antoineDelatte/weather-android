package com.example.weather.repository.retrofit

import com.example.weather.model.Location
import com.example.weather.model.Resource


interface IGeocodingHelper {

    suspend fun getLocationsByName(locationName: String): Resource<List<Location>>

    suspend fun  getLocationByCoordinates(latitude: Double, longitude: Double): Resource<Location>
}