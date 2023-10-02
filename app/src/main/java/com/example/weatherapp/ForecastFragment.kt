
package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentForecastBinding
import com.example.weatherapp.models.forecast.ForecastResult
import com.example.weatherapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding?= null
    private val binding get() = _binding
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_forecast, container, false)
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.forecastRecycler?.layoutManager = layoutManager

        mainViewModel.forecastResult.observe(viewLifecycleOwner, Observer {
            val forecastResultList = mutableListOf<ForecastResult>()
            //forEach - функция-цикл, которая применяет функцию (в данном случае add) к каждому элементу списка)
            it.list.forEach { forecastItem ->
                val forecastResult = ForecastResult(
                    main = forecastItem.weather.first().main,
                    description = forecastItem.weather.first().description,
                    temp = forecastItem.main.temp.toString(),
                    date = forecastItem.dt_txt
                )
                forecastResultList.add(forecastResult)
            }
            adapter = ForecastAdapter(requireContext(), forecastResultList)
            binding?.forecastRecycler?.adapter = adapter
        })
    }

    companion object {
        fun newInstance() = ForecastFragment()
                }
            }
