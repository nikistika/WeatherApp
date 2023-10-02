package com.example.weatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemForecastBinding
import com.example.weatherapp.models.forecast.ForecastResult
import com.example.weatherapp.repository.WeatherRepositoryImpl
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastAdapter(private val context: Context, private val forecastList: List<ForecastResult>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(forecast: ForecastResult) {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("d MMMM HH:mm", Locale.getDefault())

            val date = inputDateFormat.parse(forecast.date)
            val outputDate = date?.let { outputDateFormat.format(it) }

            binding.itemRecyclerDate.text = "Date: $outputDate"
            binding.itemRecyclerTemp.text = "Temp: ${forecast.temp}"
            binding.itemRecyclerDescription.text = "Weather: ${forecast.description}"

            val weatherImage = when (forecast.main) {
                WeatherRepositoryImpl.WEATHER_TYPE_CLEAR -> R.drawable.clear_sky
                WeatherRepositoryImpl.WEATHER_TYPE_CLOUDS -> R.drawable.clouds
                WeatherRepositoryImpl.WEATHER_TYPE_THUNDERSTORM -> R.drawable.thunderstorm
                WeatherRepositoryImpl.WEATHER_TYPE_SHOW -> R.drawable.snow
                else -> R.drawable.mist
            }

            binding.weatherImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, weatherImage))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bindItem(forecast)
    }
}