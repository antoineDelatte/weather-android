package com.example.weather.repository.mapper

import com.example.weather.repository.bindingModel.WeatherForecastBindingModel
import com.example.weather.model.WeatherForecast
import com.example.weather.model.WeatherSituation
import java.text.SimpleDateFormat

val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
fun WeatherForecastBindingModel.toWeatherForecast() =
    dateFormat.parse(current_weather.time)?.let {
        WeatherForecast(
            temperature = current_weather.temperature,
            time = it,
            weatherSituation = when (current_weather.weathercode) {
                0 -> WeatherSituation.SUNNY
                in 1..3 -> WeatherSituation.CLOUDY
                in 45..48 -> WeatherSituation.FOGGY
                in 51..55 -> WeatherSituation.DRIZZLY
                in 56..57 -> WeatherSituation.FREEZING_DRIZZLY
                in 61..65 -> WeatherSituation.RAINLY
                in 66..67 -> WeatherSituation.FREEZING_RAINLY
                in 71..77 -> WeatherSituation.SNOWLY
                in 80..82 -> WeatherSituation.RAINLY
                in 85..86 -> WeatherSituation.SNOWLY
                in 95..99 -> WeatherSituation.THUNDERSTORM
                else -> WeatherSituation.SUNNY
            }
        )
    }

