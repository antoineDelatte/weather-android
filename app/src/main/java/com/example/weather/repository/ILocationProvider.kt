package com.example.weather.repository

import com.example.weather.model.Location
import com.example.weather.model.Resource

interface ILocationProvider {

    fun getCurrentLocation(onLocationChanged: (Resource<Location>) -> Unit)
}