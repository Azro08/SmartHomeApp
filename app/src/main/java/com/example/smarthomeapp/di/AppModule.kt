package com.example.smarthomeapp.di

import android.content.Context
import com.example.smarthomeapp.data.repository.AuthRepositoryImpl
import com.example.smarthomeapp.data.repository.UsersRepositoryImpl
import com.example.smarthomeapp.domain.repository.AuthRepository
import com.example.smarthomeapp.domain.repository.UsersRepository
import com.example.smarthomeapp.util.AuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager =
        AuthManager(context)


}