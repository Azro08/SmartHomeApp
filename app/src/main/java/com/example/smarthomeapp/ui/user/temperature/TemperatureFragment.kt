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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class TemperatureFragment : Fragment(R.layout.fragment_temperature) {
    private val binding by viewBinding(FragmentTemperatureBinding::bind)
    private val viewModel: TemperatureViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTimeAndDate()
        getTemperature()
        binding.buttonRefresh.setOnClickListener {
            getTemperature()
        }

    }

    private fun setTimeAndDate() {
        val currentTime = Calendar.getInstance().time

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(currentTime)
        binding.textViewtime.text = formattedTime

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentTime)
        binding.textViewDate.text = formattedDate
    }

    private fun getTemperature() {
        lifecycleScope.launch {
            viewModel.getTemperature()
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
        val temperature = (temperatureStatsResponse.feeds[0].field1 ?: "") + "C"
        val humidity = (temperatureStatsResponse.feeds[0].field2 ?: "") + "%"
        binding.textViewTemperature.text = temperature
        binding.textViewHumidity.text = humidity
    }

    private fun handleError() {
        val temperature = "C"
        val humidity = "%"
        binding.textViewTemperature.text = temperature
        binding.textViewHumidity.text = humidity
    }
}