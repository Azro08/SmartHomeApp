package com.example.smarthomeapp.ui.user.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.data.repository.NotificationsRepository
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _notifications =
        MutableStateFlow<ScreenState<List<Notification>>>(ScreenState.Loading())
    val notifications get() = _notifications

    fun getNotifications() = viewModelScope.launch {
        notificationsRepository.getAllSavedNotifications().let {
            if (it.isNotEmpty()) _notifications.value = ScreenState.Success(it)
            else _notifications.value = ScreenState.Error("No notifications found")
        }
    }

}