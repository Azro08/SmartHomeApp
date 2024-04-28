package com.example.smarthomeapp.data.model

import com.example.smarthomeapp.domain.model.User

data class UserDto(
    val id : String = "",
    val email : String = "",
    val role : String = "",
    val fullName : String ="",
    val phoneNumber : String = "",
    val address : String = "",
    val busyTimes : List<String> = emptyList()
)
fun UserDto.toUser() = User(
    id = id,
    email = email,
    role = role,
    fullName = fullName,
    phoneNumber = phoneNumber,
    address = address,
    busyTimes = busyTimes
)