package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.models.Coordinates
import com.example.weatherapp.models.currentweather.WeatherResult
import com.example.weatherapp.models.forecast.City
import com.example.weatherapp.models.forecast.Coord
import com.example.weatherapp.models.forecast.FiveDayForecast
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.viewModel.MainViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class MainViewModelTest {

    //При работе с LiveData нужно добавить такой Rule
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val weatherRepositoryMock: WeatherRepository = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(weatherRepositoryMock)
    }


    //runBlocking используется, когда функция запустилась из коорутины. То есть тест будет запускаться в отдельной коорутине    @Test
    fun `test getCoordinates`() = runBlocking {
        //Setup
        val city = "TestCity"
        val coordinates = Coord(1.0, 1.0)
        `when`(weatherRepositoryMock.getLocationCoordinates(city)).thenReturn(coordinates)
        //Call
        viewModel.getCoordinates(city)
        //Verification
        assertEquals(coordinates, viewModel.coordinatesResult.value)
    }

    @Test
    fun `test getCurrentWeather`() = runBlocking {
        val lat = 0.0
        val lon = 0.0
        val weatherResult = WeatherResult("main", "description", 1.0, 1, 1, 1.0)
        `when`(weatherRepositoryMock.getCurrentWeather(lat, lon)).thenReturn(weatherResult)
        viewModel.getCurrentWeather(lat, lon)

        //value - содержимое LiveData
        assertEquals(weatherResult, viewModel.currentWeatherResult.value)
    }

    @Test
    fun `test getForecast`() = runBlocking {
        val lat = 0.0
        val lon = 0.0
        //emptyList() - пустой лист
        val forecast = FiveDayForecast(
            City(Coord(1.0, 1.0),"Country",1,"name",100,200,100,11),
            1, "cod", emptyList(),1
        )
        `when`(weatherRepositoryMock.getForecast(lat,lon)).thenReturn(forecast)
        viewModel.getForecast(lat,lon)
        assertEquals(forecast, viewModel.forecastResult.value)
    }

}