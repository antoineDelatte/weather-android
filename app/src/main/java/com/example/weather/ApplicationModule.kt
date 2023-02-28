package com.example.weather

import android.content.Context
import com.example.weather.repository.NoInternetInterceptor
import com.example.weather.repository.retrofit.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    private var mGsonConverterFactory: GsonConverterFactory? = null

    private val gsonConverter: GsonConverterFactory
        get() {
            if (mGsonConverterFactory == null) {
                mGsonConverterFactory = GsonConverterFactory
                    .create(
                        GsonBuilder()
                            .setLenient()
                            .disableHtmlEscaping()
                            .create()
                    )
            }
            return mGsonConverterFactory!!
        }

    @Provides
    @Singleton
    fun provideNoInternetInterceptor(@ApplicationContext context: Context): NoInternetInterceptor {
        return NoInternetInterceptor(context = context)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(noInternetInterceptor: NoInternetInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(noInternetInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
            .readTimeout(1, TimeUnit.MINUTES)    // read timeout
            .writeTimeout(1, TimeUnit.MINUTES)   // write timeout
            .build()
    }

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(gsonConverter)
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @Named("geocoding")
    fun provideGeocodingRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(gsonConverter)
            .baseUrl(BuildConfig.GEOCODING_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideWeatherService(@Named("weather") retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideGeocodingService(@Named("geocoding") retrofit: Retrofit): GeocodingService = retrofit.create(
        GeocodingService::class.java)

    @Provides
    @Singleton
    fun provideWeatherHelper(weatherHelper: WeatherHelper): IWeatherHelper = weatherHelper

    @Provides
    @Singleton
    fun provideGeocodingHelper(geocodingHelper: GeocodingHelper): IGeocodingHelper = geocodingHelper
}