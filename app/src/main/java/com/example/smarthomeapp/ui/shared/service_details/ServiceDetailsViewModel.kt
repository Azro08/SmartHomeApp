package com.example.smarthomeapp.ui.shared.service_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.data.repository.ServicesRepository
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServiceDetailsViewModel @Inject constructor(
    private val servicesRepository: ServicesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _service = MutableStateFlow<ScreenState<Service>>(ScreenState.Loading())
    val service get() = _service

    init {
        savedStateHandle.get<String>(Constants.SERVICE_KEY)?.let {
            getServiceDetails(it)
        }
    }

    private fun getServiceDetails(serviceId: String) = viewModelScope.launch {
        servicesRepository.getService(serviceId).let {
            if (it != null) _service.value = ScreenState.Success(it)
            else _service.value = ScreenState.Error("Service not found")
        }
    }

}