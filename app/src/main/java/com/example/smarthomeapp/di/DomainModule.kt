package com.example.smarthomeapp.di

import com.example.smarthomeapp.data.repository.AuthRepositoryImpl
import com.example.smarthomeapp.data.repository.UsersRepositoryImpl
import com.example.smarthomeapp.domain.repository.AuthRepository
import com.example.smarthomeapp.domain.repository.UsersRepository
import com.example.smarthomeapp.domain.usecase.LoginUseCase
import com.example.smarthomeapp.domain.usecase.RegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase =
        RegisterUseCase(repository)

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase =
        LoginUseCase(repository)

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(
        firebaseAuth, firebaseFirestore
    )

    @Provides
    fun provideUsersRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): UsersRepository = UsersRepositoryImpl(
        firebaseFirestore, firebaseAuth
    )

}