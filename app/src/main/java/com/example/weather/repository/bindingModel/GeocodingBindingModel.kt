package com.example.weather.repository.bindingModel

data class GeocodingBindingModel(
    val address: Address,
    val boundingbox: List<String>,
    val `class`: String,
    val display_name: String,
    val icon: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val osm_id: Long,
    val osm_type: String,
    val place_id: Long,
    val type: String
)