package com.multrm.presentation.weatherScreen

import com.multrm.entity.weatherScreenEntity.ItemForecast
import com.multrm.entity.weatherScreenEntity.CurrentInfo

sealed interface WeatherUiState {
    data object Initial: WeatherUiState
    data object Loading: WeatherUiState
    data class Success(
        val current: CurrentInfo,
        val forecast: List<ItemForecast>
    ): WeatherUiState
    sealed class Error(): WeatherUiState {
        data object UnknownHost: Error()
        data object NotFoundCity: Error()
        data object Timeout: Error()
        data object Other: Error()
    }
}