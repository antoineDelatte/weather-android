package com.example.weather.repository.mapper

import com.example.weather.model.Location

fun com.example.weather.repository.bindingModel.GeocodingBindingModel.toLocation() = Location(
    name = address.town ?: address.city_district ?: address.county ?: address.region,
    latitude = lat.toDouble(),
    longitude = lon.toDouble()
)