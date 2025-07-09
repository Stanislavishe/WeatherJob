package com.multrm.weather.di

import com.multrm.data.WeatherDatabaseRepositoryImpl
import com.multrm.data.WeatherRepositoryImpl
import com.multrm.domain.Operation
import com.multrm.domain.WeatherDatabaseRepository
import com.multrm.domain.WeatherRepository
import com.multrm.presentation.weatherScreen.WeatherUiMapper
import com.multrm.presentation.weatherScreen.WeatherUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    abstract fun bindRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    abstract fun bindHomeMapper(mapper: WeatherUiMapper): Operation.Mapper<WeatherUiState>

    @Binds
    abstract fun bindDBRepository(dbRepositoryImpl: WeatherDatabaseRepositoryImpl): WeatherDatabaseRepository
}