package com.multrm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.multrm.entity.db.CityWeatherFrommDb

@Dao
interface WeatherDao {
    @Query("SELECT * FROM city_weather WHERE name = :name")
    fun getWeatherInfo(name: String): List<CityWeatherFrommDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherInfo(cityWeatherFrommDb: CityWeatherFrommDb)
}