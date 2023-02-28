package com.example.weather.repository.retrofit

import com.example.weather.repository.bindingModel.WeatherForecastBindingModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
     suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("temperature_unit") unit: String = "celsius"
    ) : Response<WeatherForecastBindingModel>


}