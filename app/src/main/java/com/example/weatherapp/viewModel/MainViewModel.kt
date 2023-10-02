package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.currentweather.WeatherResult
import com.example.weatherapp.models.forecast.Coord
import com.example.weatherapp.models.forecast.FiveDayForecast
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val coordinates: MutableLiveData<Coord> = MutableLiveData()
    val coordinatesResult: LiveData<Coord> = coordinates

    private val currentWeather: MutableLiveData<WeatherResult> = MutableLiveData()
    val currentWeatherResult: LiveData<WeatherResult> = currentWeather

    private val forecast: MutableLiveData<FiveDayForecast> = MutableLiveData()
    val forecastResult: LiveData<FiveDayForecast> = forecast



    suspend fun getCoordinates(city: String) {
        withContext(Dispatchers.IO){
            val coordinatesApiResult = weatherRepository.getLocationCoordinates(city)
            coordinates.postValue(coordinatesApiResult)
        }
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double){
        withContext(Dispatchers.IO) {
            val currentWeatherResult = weatherRepository.getCurrentWeather(lat, lon)
            currentWeather.postValue(currentWeatherResult)
        }
    }

    suspend fun getForecast(lat: Double, lon: Double){
        withContext(Dispatchers.IO) {
            val currentForecast = weatherRepository.getForecast(lat, lon)
            forecast.postValue(currentForecast)
        }
    }
}