package com.example.smarthomeapp.ui.shared.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.Order
import com.example.smarthomeapp.data.repository.OrderRepository
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _allOrders = MutableStateFlow<ScreenState<List<Order>>>(ScreenState.Loading())
    val allOrders get() = _allOrders

    private val _usersOrders = MutableStateFlow<ScreenState<List<Order>>>(ScreenState.Loading())
    val usersOrder get() = _usersOrders

    init {
        getOrders()
    }

    fun getUserOrders(userEmail: String) = viewModelScope.launch {
        repository.getUsersOrders(userEmail).let {
            if (it.isNotEmpty()) _usersOrders.value = ScreenState.Success(it)
            else _usersOrders.value = ScreenState.Error("No orders found")
        }
    }

    private fun getOrders() = viewModelScope.launch {
        repository.getOrders().let {
            if (it.isNotEmpty()) _allOrders.value = ScreenState.Success(it)
            else _allOrders.value = ScreenState.Error("No orders found")
        }
    }

}