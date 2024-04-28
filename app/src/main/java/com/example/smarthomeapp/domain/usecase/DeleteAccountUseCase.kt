package com.example.smarthomeapp.domain.usecase

import com.example.smarthomeapp.domain.repository.AuthRepository
import com.example.smarthomeapp.domain.repository.UsersRepository

class DeleteAccountUseCase(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(uid: String): String {
        return try {
            usersRepository.deleteAccount(uid)
            usersRepository.deleteUsersProducts(uid)
            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }
    }


}