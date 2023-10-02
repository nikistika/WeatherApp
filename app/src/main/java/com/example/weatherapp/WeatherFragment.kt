package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.repository.WeatherRepositoryImpl
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.WEATHER_TYPE_CLEAR
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.WEATHER_TYPE_CLOUDS
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.WEATHER_TYPE_SHOW
import com.example.weatherapp.repository.WeatherRepositoryImpl.Companion.WEATHER_TYPE_THUNDERSTORM
import com.example.weatherapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    //Проверка, был ли байндинг сгенерирован системой. Необходимо, чтоб приложение не разрушилось
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.currentWeatherResult.observe(viewLifecycleOwner, Observer {
            binding?.weatherMain?.text = it.main
            binding?.weatherDescription?.text = it.description
            binding?.weatherTemp?.text = it.temp.toString()
            binding?.weatherHumidityValue?.text = it.humidity.toString()
            binding?.weatherPressureValue?.text = it.pressure.toString()
            binding?.weatherWindSpeedValue?.text = it.windSpeed.toString()

            val weatherImage = when (it.main) {
                WEATHER_TYPE_CLEAR -> R.drawable.clear_sky
                WEATHER_TYPE_CLOUDS -> R.drawable.clouds
                WEATHER_TYPE_THUNDERSTORM -> R.drawable.thunderstorm
                WEATHER_TYPE_SHOW -> R.drawable.snow
                else -> R.drawable.mist
            }

            binding?.weatherImage?.setImageDrawable(binding?.root?.let { it1 ->
                ContextCompat.getDrawable(
                    it1.context, weatherImage)
            })
        })
    }

    companion object {
        fun newInstance() = WeatherFragment()
            }
    }
