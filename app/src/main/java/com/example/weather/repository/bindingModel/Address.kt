package com.example.weather.repository.bindingModel

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("ISO3166-2-lvl4")
    val ISO31662lvl4: String?,
    @SerializedName("ISO3166-2-lvl6")
    val ISO31662lvl6: String?,
    val city_district: String?,
    val country: String?,
    val country_code: String?,
    val county: String?,
    val postcode: String?,
    val region: String?,
    val state: String?,
    val town: String?
)