package com.example.smarthomeapp.domain.repository

import com.example.smarthomeapp.domain.model.User

interface AuthRepository {

    suspend fun login(email: String, password: String): String

    suspend fun signup(email: String, password: String): String

    suspend fun saveUser(user: User): String


    suspend fun getUserRole(): String?
}