package com.example.smarthomeapp.domain.repository

interface UsersRepository {
    suspend fun deleteAccount(userId : String) : String
    suspend fun deleteUsersProducts(uid: String)
}