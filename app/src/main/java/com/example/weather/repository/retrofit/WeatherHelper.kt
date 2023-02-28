package com.example.weather.repository.retrofit

import android.util.Log
import com.example.weather.ResourceError
import com.example.weather.model.Resource
import com.example.weather.model.WeatherForecast
import com.example.weather.repository.BaseHelper
import com.example.weather.repository.bindingModel.WeatherForecastBindingModel
import com.example.weather.repository.mapper.toWeatherForecast
import retrofit2.Response
import javax.inject.Inject

class WeatherHelper @Inject constructor(private val weatherService: WeatherService): BaseHelper(),
    IWeatherHelper {

    override suspend fun getForecastByCoordinates(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherForecast> {
        return safeApiCall {
            val response: Response<WeatherForecastBindingModel> = weatherService.getWeatherForecast(latitude, longitude)
            if (response.isSuccessful) {
                response.body()?.let {data: WeatherForecastBindingModel ->
                    data.toWeatherForecast()?.let {
                        return@safeApiCall Resource.Success(it)
                    }
                }
                return@safeApiCall Resource.Error<WeatherForecast>(errorCode = ResourceError.NOT_FOUND)
            }
            return@safeApiCall Resource.Error(errorCode = ResourceError.GLOBAL_ERROR)
        }

    }


}