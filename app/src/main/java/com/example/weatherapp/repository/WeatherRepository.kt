package com.example.weatherapp.repository

import com.example.weatherapp.models.currentweather.WeatherResult
import com.example.weatherapp.models.forecast.Coord
import com.example.weatherapp.models.forecast.FiveDayForecast


/**
 * Provides API connection with https://openweathermap.org/
 */
interface WeatherRepository {

    /**
     * Getting location info like lot and lon
     */

    suspend fun getLocationCoordinates(city: String): Coord

    /**
     * Getting current weather for specific place by provide lat and lon
     */

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResult

    /**
     * Getting forecast for specific place by provide lat and lon
     */

    suspend fun getForecast(lat: Double, lon: Double): FiveDayForecast

}