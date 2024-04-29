package com.example.smarthomeapp.ui.admin.edit_master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthomeapp.data.repository.UsersRepositoryImpl
import com.example.smarthomeapp.data.model.toUser
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMasterViewModel @Inject constructor(
    private val usersRepositoryImpl: UsersRepositoryImpl
) : ViewModel() {

    private val _masterDetails = MutableStateFlow<ScreenState<User>>(ScreenState.Loading())
    val masterDetails get() = _masterDetails

    private val _allMasters = MutableStateFlow<ScreenState<List<User>>>(ScreenState.Loading())
    val allMasters get() = _allMasters

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

    fun getUser(id: String) = viewModelScope.launch {
        try {
            usersRepositoryImpl.getUser(id).let {
                if (it != null) _masterDetails.value = ScreenState.Success(it.toUser())
                else _masterDetails.value = ScreenState.Error("User details not Found")
            }
        } catch (e: Exception) {
            _masterDetails.value = ScreenState.Error(e.message.toString())
        }
    }

    fun refreshMasters() {
        getAllMasters()
    }

}