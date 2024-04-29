package com.example.smarthomeapp.ui.admin.send_notification

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
class SendNotificationViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _sendNotificationState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val sendNotificationState = _sendNotificationState

    fun sendNotification(notification: Notification) = viewModelScope.launch {
        notificationsRepository.sendNotificationAndSave(notification).let {
            if (it == "Done") _sendNotificationState.value = ScreenState.Success(it)
            else _sendNotificationState.value = ScreenState.Error(it)
        }
    }

}