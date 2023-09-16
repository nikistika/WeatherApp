package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.network.RetrofitHelper
import com.example.weatherapp.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var locationLabel: TextView
    private lateinit var currentWeatherLabel: TextView
    private lateinit var forecastLabel: TextView

    private val retrofitClient = RetrofitHelper.getInstance().create(WeatherApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationLabel = findViewById(R.id.locationLabel)
        currentWeatherLabel = findViewById(R.id.currentWeatherLabel)
        forecastLabel = findViewById(R.id.forecastLabel)


        lifecycleScope.launch(Dispatchers.IO) {
            val appleId = "ab869752f1de6b0adccb01f8d2578742"

            //Делаем первый запрос на получение координат
            //limit - количество результатов, то есть количество городов
            val result = retrofitClient.getGeocoding("London", limit = "1",appleId)

            //Мы извлекаем из результата наши координаты lat и lot
            val latResult = result.body()?.first()?.lat ?:0.0
            val lonResult = result.body()?.first()?.lon ?:0.0

            //Мы делаем второй запрос на получение текущей погоды. И передаём наши координаты.
            val currentWeather = retrofitClient.getCurrentWeather(latResult, lonResult, appleId, "metric")

            //Мы делаем третий запрос на получение погоды на несколько дней. И передаём наши координаты.
            val forecast = retrofitClient.getForecast(latResult, lonResult, appleId, "metric")

            Log.d("MyLog", "geoCoding --> ${result.body()}")
            Log.d("MyLog", "currentWeather --> ${currentWeather.body()}")
            Log.d("MyLog", "forecast --> ${forecast.message()}")
            Log.d("MyLog", "forecast --> ${forecast.isSuccessful}")
            Log.d("MyLog", "forecast --> ${forecast.body()}")


            withContext(Dispatchers.Main) {
                locationLabel.text = "Location: $latResult $lonResult"
                currentWeatherLabel.text = currentWeather.body()?.weather?.first()?.description ?: ""
                forecastLabel.text = forecast.body()?.list?.first()?.weather?.first()?.description ?: ""
            }
        }
    }
}