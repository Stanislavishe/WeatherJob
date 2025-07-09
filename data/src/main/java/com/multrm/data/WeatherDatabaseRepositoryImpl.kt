package com.multrm.data

import android.content.SharedPreferences
import com.multrm.domain.WeatherDatabaseRepository
import javax.inject.Inject
import androidx.core.content.edit
import com.multrm.data.db.WeatherDao
import com.multrm.domain.Operation
import com.multrm.entity.db.CityWeatherFrommDb
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast

class WeatherDatabaseRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val dao: WeatherDao
): WeatherDatabaseRepository {
    override fun setImHere(value: String?) =
        sharedPreferences.edit { putString(IM_HERE, value) }

    override fun getImHereCity() = sharedPreferences.getString(IM_HERE, null)

    override suspend fun getCacheWeather(name: String): Operation<List<CityWeatherFrommDb>> {
        return try {
            val data = dao.getWeatherInfo(name)
            Operation.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Operation.Failure(e)
        }
    }

    override suspend fun putCacheWeather(name: String, current: CurrentInfo, forecast: List<ItemForecast>) {
        val weather = CityWeatherFrommDb(current = current, forecast = forecast, name = name)
        dao.insertWeatherInfo(weather)
    }

    companion object {
        private const val IM_HERE = "IM_HERE"
    }
}