package com.multrm.domain

import com.multrm.entity.CityWeather

interface WeatherRepository {
    suspend fun getCitiesList(): List<String>
    suspend fun getWeather(cityName: String, days: Int): Operation<CityWeather>
}