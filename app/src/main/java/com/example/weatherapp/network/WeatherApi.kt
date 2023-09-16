package com.example.weatherapp.network

import com.example.weatherapp.model.currentweather.CurrentWeather
import com.example.weatherapp.model.forecast.FiveDayForecast
import com.example.weatherapp.model.forecast.Forecast
import com.example.weatherapp.model.geocoding.Geocoding
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/geo/1.0/direct")
    suspend fun getGeocoding(
        @Query("q") location: String,
        @Query("limit") limit: String,
        @Query("appId") appId: String
    ): Response<Geocoding>

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String,
    ): Response<CurrentWeather>

    @GET("/data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String,
    ): Response<FiveDayForecast>
}