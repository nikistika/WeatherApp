package com.example.weatherapp

import com.example.weatherapp.models.Coordinates
import com.example.weatherapp.models.currentweather.Clouds
import com.example.weatherapp.models.currentweather.Coord
import com.example.weatherapp.models.currentweather.CurrentWeather
import com.example.weatherapp.models.currentweather.Main
import com.example.weatherapp.models.currentweather.Sys
import com.example.weatherapp.models.currentweather.Weather
import com.example.weatherapp.models.currentweather.Wind
import com.example.weatherapp.models.forecast.City
import com.example.weatherapp.models.forecast.FiveDayForecast
import com.example.weatherapp.models.forecast.Forecast
import com.example.weatherapp.models.forecast.Rain
import com.example.weatherapp.models.geocoding.Geocoding
import com.example.weatherapp.models.geocoding.GeocodingItem
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.repository.WeatherRepositoryImpl
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.APP_ID
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.LIMIT
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.METRIC
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response
import junit.framework.Assert.assertEquals


class WeatherRepositoryImplTest {

    private val weatherApiMock: WeatherApi = mock()

    private lateinit var subject: WeatherRepositoryImpl

    private val lat = 37.7749
    private val lon = -122.4194

    @Before
    fun setUp(){
        subject = WeatherRepositoryImpl(weatherApiMock)
    }

    @Test
    fun getLocationCoordinates_success(): Unit = runBlocking {
        val city = "London"
        val expectedCoordinates = Coordinates(37.7749, -122.4194)
        val location = Geocoding()
        location.add(GeocodingItem("USA", 37.7749, null, -122.4194,
            "San Francisco", "CA"))
        location.add(GeocodingItem("USA", 40.7128, null, -74.0060,
            "New York", "NY"))

        //WeatherApi возвращает именно Response, поэтому мы мокаем его
        val mockResponse = Response.success(location)

        `when`(weatherApiMock.getGeocoding(city, LIMIT, APP_ID)).thenReturn(mockResponse)

        val result = subject.getLocationCoordinates(city)

        assertEquals(expectedCoordinates.lat ,result.lat)
        assertEquals(expectedCoordinates.lon ,result.lon)
    }

    @Test
    fun getCurrentWeather_success(): Unit = runBlocking {
        val location = CurrentWeather(
            "base",
            Clouds(1), 1, Coord(lat, lon), 1, 1,
            Main(1.0, 1, 1, 1, 1, 1.0, 1.0,
                1.0), "name",
            Sys(
                "country", 1, 1, 1, 1
            ), 1, 1,
            listOf(Weather("description", "icon", 1, "main")), Wind(1,
                1.0, 1.0)
        )

        val mockResponse = Response.success(location)

        `when`(weatherApiMock.getCurrentWeather(lat, lon, APP_ID, METRIC)).thenReturn(mockResponse)

        val result = subject.getCurrentWeather(lat, lon)

        assertEquals("main", result.main)
        assertEquals("description", result.description)
    }

    @Test
    fun getForecast_success() = runBlocking {
        val expectedCity = City(com.example.weatherapp.models.forecast.Coord(lat, lon),
            "San Francisco", 123, "US", 1620351906, 1620393328,
            3600, -25200)
        val expectedForecast = FiveDayForecast(expectedCity, 40, "cod", listOf(
            Forecast(
                com.example.weatherapp.models.forecast.Clouds(1), 1, "dt",
                com.example.weatherapp.models.forecast.Main(1.0, 1, 1, 1,
                    1, 1.0, 1.0, 1.0, 1.0), 1.0,
                Rain(1.1), com.example.weatherapp.models.forecast.SysForecast(
                    "pod"),1, listOf(com.example.weatherapp.models.forecast.Weather(
                    "description","icon",1,"Main")),
                com.example.weatherapp.models.forecast.Wind(1, 1.0, 1.0)
            )
        ) , 1)

        val mockResponse = Response.success(expectedForecast)
        `when`(weatherApiMock.getForecast(lat, lon, APP_ID, METRIC)).thenReturn(mockResponse)


        val actualForecast = subject.getForecast(lat, lon)

        assertEquals(expectedForecast, actualForecast)
    }

}