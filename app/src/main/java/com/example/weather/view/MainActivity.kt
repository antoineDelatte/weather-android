package com.example.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.NotGrantedPermissionException
import com.example.weather.R
import com.example.weather.ResourceError
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.model.Resource
import com.example.weather.model.WeatherForecast
import com.example.weather.model.WeatherSituation
import com.example.weather.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {

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

        binding.currentLocationButton.setOnClickListener(this)
    }

    private fun resetAll() {
        binding.loading.visibility = INVISIBLE
        binding.imageError.visibility = INVISIBLE
        binding.locationNameTextView.visibility = INVISIBLE
        binding.temperatureTextView.visibility = INVISIBLE
    }

    private fun onSuccess(weatherForecast: WeatherForecast) {
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
        val locationName: String = if (weatherForecast.location.name != null) {
            weatherForecast.location.name!!
        } else {
            binding.searchView.query.toString()
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
            ResourceError.CURRENT_LOCATION_NOT_FOUND -> ContextCompat.getDrawable(baseContext, R.drawable.location_not_found_error)
            ResourceError.GLOBAL_ERROR -> ContextCompat.getDrawable(baseContext, R.drawable.global_error)
        }
        binding.imageError.setImageDrawable(drawable)
        binding.imageError.visibility = VISIBLE
        binding.background.visibility = INVISIBLE
    }

    private fun onLoading() {
        binding.loading.visibility = VISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.currentLocationButton -> {
                resetAll()
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                getForecastForCurrentLocation()
            }
        }
    }

    private fun getForecastForCurrentLocation() {
        val locationPermissionGranted = checkLocationPermission()
        if (locationPermissionGranted) {
            try {
                weatherViewModel.getForecastForCurrentLocation()
            } catch (e: NotGrantedPermissionException) {
                getForecastForCurrentLocation()
            }
        } else {
            askLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("permission result", requestCode.toString() + permissions.toString() + grantResults.toString())
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.any { result: Int -> result == PackageManager.PERMISSION_GRANTED }) {
                getForecastForCurrentLocation()
            } else {
                binding.currentLocationButton.visibility = GONE
            }
        }
    }


    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), LOCATION_PERMISSIONS_REQUEST_CODE)
    }

    companion object {
        private const val LOCATION_PERMISSIONS_REQUEST_CODE = 1
    }

}