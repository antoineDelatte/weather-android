package com.example.weather.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.weather.R
import com.example.weather.ResourceError
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.model.Resource
import com.example.weather.model.WeatherForecast
import com.example.weather.model.WeatherSituation
import com.example.weather.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        weatherViewModel.weatherForecast.observe(this) {
            resetAll()
            when(it) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(it.data!!)
                is Resource.Error -> onError(it.errorCode!!)
                else -> onError(ResourceError.GLOBAL_ERROR)
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    weatherViewModel.getForecastByLocationName(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun resetAll() {
        binding.loading.visibility = INVISIBLE
        binding.imageError.visibility = INVISIBLE
        binding.locationNameTextView.visibility = INVISIBLE
        binding.temperatureTextView.visibility = INVISIBLE
    }

    private fun onSuccess(weatherForecast: WeatherForecast) {
        var locationName: String

        val backgroundImage = when (weatherForecast.weatherSituation) {
            WeatherSituation.SUNNY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.sunny
            )
            WeatherSituation.CLOUDY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.cloudy
            )
            WeatherSituation.FOGGY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.foggy
            )
            WeatherSituation.DRIZZLY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.drizzle
            )
            WeatherSituation.FREEZING_DRIZZLY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.freezing_drizzle
            )
            WeatherSituation.RAINLY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.rainly
            )
            WeatherSituation.FREEZING_RAINLY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.freezing_rainly
            )
            WeatherSituation.SNOWLY -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.snowly
            )
            WeatherSituation.THUNDERSTORM -> ContextCompat.getDrawable(
                baseContext,
                R.drawable.thunderstorm
            )
        }
        if (weatherForecast.location.name != null) {
            locationName = weatherForecast.location.name!!
        } else {
            locationName = binding.searchView.query.toString()
        }
        binding.locationNameTextView.text = locationName
        binding.locationNameTextView.visibility = VISIBLE
        binding.temperatureTextView.text = getString(R.string.temperature_format, weatherForecast.temperature)
        binding.temperatureTextView.visibility = VISIBLE
        binding.background.setImageDrawable(backgroundImage)
        binding.background.visibility = VISIBLE
    }

    private fun onError(errorCode: ResourceError) {
        val drawable = when(errorCode) {
            ResourceError.NOT_FOUND -> ContextCompat.getDrawable(baseContext, R.drawable.not_found_error)
            ResourceError.INTERNET_ERROR -> ContextCompat.getDrawable(baseContext, R.drawable.no_internet_error)
            ResourceError.GLOBAL_ERROR -> ContextCompat.getDrawable(baseContext, R.drawable.global_error)
        }
        binding.imageError.setImageDrawable(drawable)
        binding.imageError.visibility = VISIBLE
        binding.background.visibility = INVISIBLE
    }

    private fun onLoading() {
        binding.loading.visibility = VISIBLE
    }

}