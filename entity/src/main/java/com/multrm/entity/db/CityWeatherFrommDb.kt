package com.multrm.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast

@Entity(tableName = "city_weather")
data class CityWeatherFrommDb(
    @PrimaryKey
    val name: String,
    val current: CurrentInfo? = null,
    val forecast: List<ItemForecast>? = null
)
