package com.example.smarthomeapp.ui.user.temperature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.TemperatureStatsResponse
import com.example.smarthomeapp.data.repository.TemperatureRepository
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemperatureViewModel @Inject constructor(
    private val temperatureRepository: TemperatureRepository
) : ViewModel() {

    private val _temperature =
        MutableStateFlow<ScreenState<TemperatureStatsResponse>>(ScreenState.Loading())
    val temperature = _temperature

    private val _setTemperature = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val setTemperature = _setTemperature

    init {
        getTemperature()
    }

    fun getTemperature() = viewModelScope.launch {
        temperatureRepository.getTemperatureStats().let {
            if (it != null) _temperature.value = ScreenState.Success(it)
            else _temperature.value = ScreenState.Error("Error")
        }
    }

    fun setTemperature(temperature: Int, humidity : Int) = viewModelScope.launch {
        temperatureRepository.updateTemperatureStats(temperature, humidity).let {
            if (it == "Done") _setTemperature.value = ScreenState.Success(it)
            else _setTemperature.value = ScreenState.Error("Error")
        }
    }

}