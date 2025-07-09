package com.multrm.presentation.homeScreen

import com.multrm.entity.CityWeather

sealed interface HomeUiState {
    data object Initial: HomeUiState
    data object Loading: HomeUiState
    data class Success(val data: List<String>): HomeUiState
    data class Error(val exception: Exception): HomeUiState
}