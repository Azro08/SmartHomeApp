package com.example.smarthomeapp.domain.usecase

import com.example.smarthomeapp.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): String {

        return try {
            authRepository.login(email, password).let {
                if (it == "Done") authRepository.getUserRole().let { role ->
                    if (!role.isNullOrEmpty()) role
                    else "User role can't be found!"
                }
                else it
            }
        } catch (e: Exception) {
            e.message.toString()
        }

    }

}