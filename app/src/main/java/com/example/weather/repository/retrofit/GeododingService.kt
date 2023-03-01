package com.example.weather.repository.retrofit

import com.example.weather.repository.bindingModel.GeocodingBindingModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {

    @GET("search")
    suspend fun getLocationsByName(
        @Query("q") locationName: String,
        @Query("format") format:String = "json",
        @Query("addressdetails") addressDetails: Int = 1) : Response<List<GeocodingBindingModel>>

    @GET("reverse")
    suspend fun getLocationsByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format:String = "json",
        @Query("addressdetails") addressDetails: Int = 1) : Response<GeocodingBindingModel>
}