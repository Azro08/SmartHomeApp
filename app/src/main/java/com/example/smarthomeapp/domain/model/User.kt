package com.example.smarthomeapp.domain.model

import com.example.smarthomeapp.data.model.UserDto

data class User(
    var id : String = "",
    val email : String = "",
    val role : String = "",
    val fullName : String = "",
    val phoneNumber : String = "",
    val address : String = "",
)

fun User.toUserDto() = UserDto(
    id = id,
    email = email,
    role = role,
    fullName = fullName,
    address = address,
    phoneNumber = phoneNumber
)