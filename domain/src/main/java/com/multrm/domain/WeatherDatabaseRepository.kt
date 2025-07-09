package com.multrm.domain

import com.multrm.entity.db.CityWeatherFrommDb
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast

interface WeatherDatabaseRepository {
    fun setImHere(value: String?)
    fun getImHereCity(): String?

    suspend fun getCacheWeather(name: String): Operation<List<CityWeatherFrommDb>>
    suspend fun putCacheWeather(
        name: String,
        current: CurrentInfo,
        forecast: List<ItemForecast>
    )
}