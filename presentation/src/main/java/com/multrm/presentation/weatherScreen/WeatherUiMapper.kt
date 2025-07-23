package com.multrm.presentation.weatherScreen

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

class WeatherUiMapper @Inject constructor() : Operation.Mapper<WeatherUiState> {
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
                    val temp = ItemForecast.Temperature(
                        R.string.forecastTemp,
                        forecast.day.minTemp.toInt(),
                        forecast.day.maxTemp.toInt()
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

    private fun convertWindDir(windDir: String): Int {
        return when (windDir) {
            "S" -> R.string.dir_s
            "W" -> R.string.dir_w
            "N" -> R.string.dir_n
            "E" -> R.string.dir_e
            "SW" -> R.string.dir_sw
            "SE" -> R.string.dir_se
            "NW" -> R.string.dir_nw
            "NE" -> R.string.dir_ne
            "SSW" -> R.string.dir_ssw
            "SSN" -> R.string.dir_ssn
            "WSW" -> R.string.dir_wsw
            "NEE" -> R.string.dir_nee
            "ENE" -> R.string.dir_ene
            "ESE" -> R.string.dir_ese
            "WNW" -> R.string.dir_wnw
            "NNW" -> R.string.dir_nnw
            else -> {
                R.string.unknown_dir
            }
        }
    }

    private fun buildCurrentList(current: CityWeather.Current) = buildList {
        add(
            ItemInfo(
                titleResId = R.string.temperature,
                value = current.tempC.toInt(),
                measurementResId = R.string.c
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.filslike,
                value = current.feelsLikeC.toInt(),
                measurementResId = R.string.c
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.wind,
                value = current.winKph.toInt(),
                measurementResId = R.string.kph
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.gustsWind,
                value = current.gustKph.toInt(),
                measurementResId = R.string.kph
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.pressure,
                value = current.pressureMb.toInt(),
                measurementResId = R.string.mbar
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.humidity,
                value = current.humidity,
                measurementResId = R.string.procent
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.dewPoint,
                value = current.dewPointC.toInt(),
                measurementResId = R.string.c
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.uvIndex,
                value = current.uv.toInt(),
                measurementResId = 0
            )
        )
        add(
            ItemInfo(
                titleResId = R.string.direction,
                value = convertWindDir(current.windDir),
                measurementResId = 0
            )
        )
    }
}