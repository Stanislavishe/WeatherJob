package com.multrm.presentation.weatherScreen

import android.content.Context
import android.util.Log
import com.multrm.domain.Operation
import com.multrm.entity.CityWeather
import com.multrm.entity.db.CityWeatherFrommDb
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast
import com.multrm.entity.weatherScreenEntity.ItemInfo
import com.multrm.presentation.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherUiMapper @Inject constructor(
    private val context: Context,
) : Operation.Mapper<WeatherUiState> {
    override fun <T> mapSuccess(data: T): WeatherUiState {
        return if (data is CityWeather) {
            val weatherState = data.current.condition.text
            val weatherIcon = "https:" + data.current.condition.icon
            val currentList = buildCurrentList(data.current)

            val currentInfo = CurrentInfo(weatherState, weatherIcon, currentList)
            val forecastList = buildList {
                data.forecast.forecastday.forEach { forecast ->
                    val convertedDate = forecast.date.takeLast(5).replace("-", ".")
                    val icon = "https:" + forecast.day.condition.icon
                    val temp = context.getString(
                        R.string.forecastTemp,
                        forecast.day.minTemp.toInt().toString(),
                        forecast.day.maxTemp.toInt().toString()
                    )
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

    private fun buildCurrentList(current: CityWeather.Current) = buildList {
        add(
            ItemInfo(
                title = context.getString(R.string.temperature),
                value = current.tempC.toInt().toString(),
                measurement = context.getString(R.string.c)
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.filslike),
                value = current.feelsLikeC.toInt().toString(),
                measurement = context.getString(R.string.c)
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.wind),
                value = current.winKph.toInt().toString(),
                measurement = context.getString(R.string.kph)
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.gustsWind),
                value = current.gustKph.toInt().toString(),
                measurement = context.getString(R.string.kph)
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.pressure),
                value = current.pressureMb.toInt().toString(),
                measurement = context.getString(R.string.mbar)
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.humidity),
                value = current.humidity.toString(),
                measurement = "%"
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.dewPoint),
                value = current.dewPointC.toInt().toString(),
                measurement = context.getString(R.string.c)
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.uvIndex),
                value = current.uv.toInt().toString(),
                measurement = ""
            )
        )
        add(
            ItemInfo(
                title = context.getString(R.string.direction),
                value = convertWindDir(current.windDir),
                measurement = ""
            )
        )
    }
}