package com.example.smarthomeapp.ui.shared.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.data.repository.ServicesRepository
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val repository: ServicesRepository
) : ViewModel() {

    private val _services = MutableStateFlow<ScreenState<List<Service>>>(ScreenState.Loading())
    val services get() = _services

    init {
        getServices()
    }

    private fun getServices() = viewModelScope.launch {
        repository.getServices().let {
            if (it.isNotEmpty()) _services.value = ScreenState.Success(it)
            else _services.value = ScreenState.Error("No services found")
        }
    }

}