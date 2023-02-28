package com.example.weather.model

data class Location(
    val latitude: Double,
    val longitude: Double
) {

    var name: String? = null

    constructor(latitude: Double, longitude: Double, name: String?) : this(latitude, longitude) {
        this.name = name
    }
}
