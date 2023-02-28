package com.example.weather.model

import java.util.Date

data class WeatherForecast(
    val temperature: Double,
    val time: Date,
    val weatherSituation: WeatherSituation) {
    lateinit var location: Location
}