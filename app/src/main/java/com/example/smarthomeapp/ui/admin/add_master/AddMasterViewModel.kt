package com.example.smarthomeapp.ui.admin.add_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.repository.UsersRepositoryImpl
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.domain.model.toUserDto
import com.example.smarthomeapp.domain.usecase.RegisterUseCase
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMasterViewModel @Inject constructor(
    private val usersRepository: UsersRepositoryImpl,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _addMasterState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val addMasterState get() = _addMasterState

    fun masterExists(user: User): Boolean {
        var userExists = false
        viewModelScope.launch {
            userExists = usersRepository.getUsers().contains(user.toUserDto())
        }
        return userExists
    }

    fun register(user: User, password: String) = viewModelScope.launch {
        registerUseCase.invoke(user, password).let {
            if (it == "Done") _addMasterState.value = ScreenState.Success(it)
            else _addMasterState.value = ScreenState.Error(it)
        }
    }

}