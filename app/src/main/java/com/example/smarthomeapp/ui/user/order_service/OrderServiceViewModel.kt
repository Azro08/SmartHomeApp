package com.example.smarthomeapp.ui.user.order_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.Order
import com.example.smarthomeapp.data.model.toUser
import com.example.smarthomeapp.data.repository.OrderRepository
import com.example.smarthomeapp.data.repository.ServicesRepository
import com.example.smarthomeapp.data.repository.UsersRepositoryImpl
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderServiceViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val servicesRepository: ServicesRepository
) : ViewModel() {

    private val _allMasters = MutableStateFlow<ScreenState<List<User>>>(ScreenState.Loading())
    val allMasters get() = _allMasters

    private val _addOrderState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val addOrderState get() = _addOrderState

    suspend fun getServices(serviceId : String) = servicesRepository.getService(serviceId)

    init {
        getAllMasters()
    }

    private fun getAllMasters() = viewModelScope.launch {
        usersRepositoryImpl.getMasters().let { userDtos ->
            if (userDtos.isNotEmpty()) _allMasters.value =
                ScreenState.Success(userDtos.map { it.toUser() })
            else _allMasters.value = ScreenState.Error("No Masters Found")
        }
    }

    fun updateMastersBusyTimes(busyTimes: List<String>, masterId: String) = viewModelScope.launch {
        usersRepositoryImpl.updateMasterBusyTimes(busyTimes, masterId)
    }

    fun addOrder(order: Order) = viewModelScope.launch {
        repository.saveOrder(order).let {
            if (it == "Done") _addOrderState.value = ScreenState.Success(it)
            else _addOrderState.value = ScreenState.Error(it)
        }
    }

}