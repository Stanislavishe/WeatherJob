package com.multrm.weather.di

import com.multrm.presentation.weatherScreen.DispatchersList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    fun provideDispatchersList(): DispatchersList = DispatchersList.Base()
}