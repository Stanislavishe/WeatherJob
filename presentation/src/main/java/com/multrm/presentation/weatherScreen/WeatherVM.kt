package com.multrm.presentation.weatherScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multrm.domain.Operation
import com.multrm.domain.WeatherDatabaseRepository
import com.multrm.domain.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherVM @Inject constructor(
    private val repository: WeatherRepository,
    private val dbRepository: WeatherDatabaseRepository,
    private val mapper: Operation.Mapper<WeatherUiState>,
    private val dispatchers: DispatchersList
) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getWeather(cityName: String) = viewModelScope.launch(dispatchers.ui()) {
        _uiState.value = WeatherUiState.Loading
        val state = repository.getWeather(cityName, 4).map(mapper)
        when (state) {
            is WeatherUiState.Success -> {
                try {
                    withContext(dispatchers.io()) {
                        dbRepository.putCacheWeather(cityName, state.current, state.forecast)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _uiState.value = state
                }
            }
            is WeatherUiState.Error.UnknownHost -> {
                withContext(dispatchers.io()) {
                    val newState = dbRepository.getCacheWeather(cityName).map(mapper)
                    _uiState.value = newState
                }
            }
            else -> {
                _uiState.value = state
            }
        }
    }

    fun getImHereCity() = dbRepository.getImHereCity()
    fun setImHere(value: String?) = dbRepository.setImHere(value)
}

interface DispatchersList {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base : DispatchersList {
        override fun io(): CoroutineDispatcher = Dispatchers.IO

        override fun ui(): CoroutineDispatcher = Dispatchers.Main
    }
}