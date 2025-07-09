package com.multrm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.multrm.entity.db.CityWeatherFrommDb
import com.multrm.entity.db.converters.CurrentInfoConverter
import com.multrm.entity.db.converters.ItemForecastConverter

@TypeConverters(CurrentInfoConverter::class, ItemForecastConverter::class)
@Database(
    entities = [
        CityWeatherFrommDb::class
    ], version = 1
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}