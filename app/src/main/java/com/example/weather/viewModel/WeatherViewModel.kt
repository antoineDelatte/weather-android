package com.example.weather.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.NotGrantedPermissionException
import com.example.weather.ResourceError
import com.example.weather.model.WeatherForecast
import com.example.weather.repository.WeatherRepository
import com.example.weather.model.Resource
import com.example.weather.repository.ILocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: ILocationProvider
) : ViewModel() {

    private val _weatherForecast = MutableLiveData<Resource<WeatherForecast>>()
    val weatherForecast: LiveData<Resource<WeatherForecast>> = _weatherForecast

    fun getForecastByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()

            val result = weatherRepository.getForecastByCoordinates(latitude, longitude)
            _weatherForecast.value = result
        }
    }

    fun getForecastByLocationName(locationName: String) {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()

            val result = weatherRepository.getForecastByLocationName(locationName)
            _weatherForecast.value = result
        }
    }

    @Throws(NotGrantedPermissionException::class)
    fun getForecastForCurrentLocation() {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            locationProvider.getCurrentLocation { it
                if (it is Resource.Success) {
                    getForecastByCoordinates(it.data!!.latitude, it.data.longitude)
                } else {
                    _weatherForecast.value = Resource.Error(ResourceError.CURRENT_LOCATION_NOT_FOUND)
                }
            }
        }

    }

}