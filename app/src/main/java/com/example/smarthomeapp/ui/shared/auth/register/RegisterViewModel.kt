package com.example.smarthomeapp.ui.shared.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azrosk.data.repository.UsersRepositoryImpl
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val usersRepositoryImpl: UsersRepositoryImpl
) : ViewModel() {


    private val _registerState = MutableStateFlow("")
    val registerState = _registerState

    fun register(user: User, password: String) = viewModelScope.launch {
        registerUseCase.invoke(user, password).let {
            _registerState.value = it
        }
    }

}