package com.multrm.presentation.weatherScreen

import android.util.Log
import com.multrm.domain.Operation
import com.multrm.entity.CityWeather
import com.multrm.entity.db.CityWeatherFrommDb
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast
import com.multrm.entity.weatherScreenEntity.ItemInfo
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherUiMapper @Inject constructor() : Operation.Mapper<WeatherUiState> {
    override fun <T> mapSuccess(data: T): WeatherUiState {
        return if (data is CityWeather) {
            val weatherState = data.current.condition.text
            val weatherIcon = "https:" + data.current.condition.icon
            val currentList = buildList {
                add(ItemInfo("Температура", data.current.tempC.toString(), "C"))
                add(ItemInfo("Ощущается как", data.current.feelsLikeC.toString(), "C"))
                add(ItemInfo("Ветер", data.current.winKph.toString(), "Км/ч"))
                add(ItemInfo("Порывы ветра", data.current.gustKph.toString(), "Км/ч"))
                add(ItemInfo("Давление", data.current.pressureMb.toString(), "Мбар"))
                add(ItemInfo("Влажность", data.current.tempC.toString(), "%"))
                add(ItemInfo("Точка росы", data.current.dewPointC.toString(), "C"))
                add(ItemInfo("Уф индекс", data.current.uv.toString(), ""))
                add(ItemInfo("Направление", convertWindDir(data.current.windDir), ""))
            }
            val currentInfo = CurrentInfo(
                weatherState, weatherIcon, currentList
            )

            val forecastList = buildList {
                data.forecast.forecastday.forEach { forecast ->
                    val convertedDate = forecast.date.takeLast(5).replace("-", ".")
                    val icon = "https:" + forecast.day.condition.icon
                    val temp = forecast.day.minTemp.toInt()
                        .toString() + ".." + forecast.day.maxTemp.toInt().toString() + " C"
                    add(ItemForecast(convertedDate, icon, temp))
                }
            }

            WeatherUiState.Success(currentInfo, forecastList)
        } else if (
            (data as? List<CityWeatherFrommDb>)?.lastOrNull()?.forecast != null &&
            (data as? List<CityWeatherFrommDb>)?.lastOrNull()?.current != null
        ) {
            data.last().let { WeatherUiState.Success(it.current!!, it.forecast!!) }
        } else {
            WeatherUiState.Error.UnknownHost
        }
    }

    override fun mapError(exception: Exception): WeatherUiState {
        return when (exception) {
            is IllegalArgumentException -> WeatherUiState.Error.NotFoundCity
            is UnknownHostException -> WeatherUiState.Error.UnknownHost
            is SocketTimeoutException -> WeatherUiState.Error.Timeout
            else -> WeatherUiState.Error.Other.also { exception.printStackTrace() }
        }
    }

    private fun convertWindDir(windDir: String): String {
        return when (windDir) {
            "S" -> "Юг"
            "W" -> "Запад"
            "N" -> "Север"
            "E" -> "Восток"
            "SW" -> "Юго-запад"
            "SE" -> "Юго-восток"
            "NW" -> "Северо-запад"
            "NE" -> "Северо-восток"
            "SSW" -> "Юго-юго-запад"
            "SSN" -> "Юго-юго-восток"
            "WSW" -> "Запад-юго-запад"
            "NEE" -> "Северо-северо-восток"
            "ENE" -> "Востоко-северо-восток"
            "ESE" -> "Востоко-юго-восток"
            "WNW" -> "Западо-северо-запад"
            "NNW" -> "Северо-северо-запад"
            else -> {
                Log.d("WeatherUiMapper", "convertWindDir: Unknown: $windDir")
                "Неизвестное значение"
            }
        }
    }
}