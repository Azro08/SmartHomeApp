package com.example.smarthomeapp.ui.admin.add_service

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.data.repository.ServicesRepository
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddServiceViewModel @Inject constructor(
    private val servicesRepository: ServicesRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _addServiceState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val addServiceState get()= _addServiceState

    private val _getServiceState = MutableStateFlow<ScreenState<Service>>(ScreenState.Loading())
    val getServiceState get() = _getServiceState

    init {
        savedStateHandle.get<String>(Constants.SERVICE_KEY)?.let {
            getService(it)
        }
    }

    private fun getService(serviceId: String) = viewModelScope.launch {
        servicesRepository.getService(serviceId).let {
            if (it != null) _getServiceState.value = ScreenState.Success(it)
            else _getServiceState.value = ScreenState.Error("Service not found")
        }
    }

    fun saveService(service : Service) = viewModelScope.launch {
        servicesRepository.addService(service)
    }

}