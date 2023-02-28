package com.example.weather.repository.retrofit

import com.example.weather.model.Resource
import com.example.weather.model.WeatherForecast

interface IWeatherHelper {

    suspend fun getForecastByCoordinates (latitude: Double, longitude: Double): Resource<WeatherForecast>
}