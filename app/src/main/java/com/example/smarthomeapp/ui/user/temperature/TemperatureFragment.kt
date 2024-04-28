package com.example.smarthomeapp.ui.user.temperature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.TemperatureStatsResponse
import com.example.smarthomeapp.databinding.FragmentTemperatureBinding
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TemperatureFragment : Fragment(R.layout.fragment_temperature) {
    private val binding by viewBinding(FragmentTemperatureBinding::bind)
    private val viewModel: TemperatureViewModel by viewModels()
    private var currentTemp = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTemperature()

        binding.imageButtonIncTemp.setOnClickListener {
            if (currentTemp < 30) {
                currentTemp++
                binding.textViewEditedTemp.text = currentTemp.toString()
            }
        }
        binding.imageButtonDecTemp.setOnClickListener {
            if (currentTemp > 10) {
                currentTemp--
                binding.textViewEditedTemp.text = currentTemp.toString()
            }
        }

        binding.buttonSetTemp.setOnClickListener {
            viewModel.setTemperature(binding.textViewTemperature.text.toString().toInt())
            getTemperature()
        }
    }

    private fun getTemperature() {
        lifecycleScope.launch {
            viewModel.temperature.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> handleError()
                    is ScreenState.Success -> {
                        displayTemperature(state.data!!)
                    }
                }
            }
        }
    }

    private fun displayTemperature(temperatureStatsResponse: TemperatureStatsResponse) {
        val latestTemperature = temperatureStatsResponse.feeds.size - 1
        val temperature = temperatureStatsResponse.feeds[latestTemperature].field1 + "C"
        val humidity = temperatureStatsResponse.feeds[latestTemperature].field2 + "%"
        binding.textViewTemperature.text = temperature
        binding.textViewHumidity.text = humidity
        binding.textViewEditedTemp.text = temperatureStatsResponse.feeds[latestTemperature].field1
        currentTemp = temperatureStatsResponse.feeds[latestTemperature].field1.toInt()
    }

    private fun handleError() {
        val temperature = "20C"
        val humidity = "50%"
        currentTemp = 20
        binding.textViewTemperature.text = temperature
        binding.textViewHumidity.text = humidity
        binding.textViewEditedTemp.text = temperature

    }
}