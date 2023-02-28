package com.example.weather.repository.bindingModel

data class CurrentWeather(
    val temperature: Double,
    val time: String,
    val weathercode: Int,
    val winddirection: Double,
    val windspeed: Double
)