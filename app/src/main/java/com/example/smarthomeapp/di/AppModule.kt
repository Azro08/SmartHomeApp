package com.example.smarthomeapp.di

import android.content.Context
import com.example.smarthomeapp.util.AuthManager
import com.example.smarthomeapp.util.FCMTokenManager
import com.example.smarthomeapp.util.LocaleUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager =
        AuthManager(context)

    @Provides
    fun provideFCMUtil(@ApplicationContext context: Context) = FCMTokenManager(context)

    @Provides
    @Singleton
    fun provideLocaleInstance(@ApplicationContext context: Context): LocaleUtils =
        LocaleUtils(context)

}