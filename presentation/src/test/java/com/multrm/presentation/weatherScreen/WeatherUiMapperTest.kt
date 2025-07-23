package com.multrm.presentation.weatherScreen

import com.multrm.entity.CityWeather
import com.multrm.entity.db.CityWeatherFrommDb
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.Test

class WeatherUiMapperTest {
    val fakeCityWeather = CityWeather(
        location = CityWeather.Location(
            country = "", lat = 0.0, lon = 0.0, localtime = "", localtimeEpoch = 0,
            name = "", region = "", tzId = ""
        ),
        current = CityWeather.Current(
            condition = CityWeather.Current.Condition(
                code = 0, icon = "", text = ""
            ),
            feelsLikeC = 0.0, gustKph = 0.0, humidity = 0, lastUpdatedEpoch = 0,
            pressureMb = 0.0, tempC = 0.0, uv = 0.0, winKph = 0.0, windDir = "", dewPointC = 0.0
        ),
        forecast = CityWeather.ForecastDay(
            listOf(
                CityWeather.ForecastDay.ForecastDayItem(
                    date = "",
                    CityWeather.ForecastDay.ForecastDayItem.Day(
                        maxTemp = 0.0,
                        0.0,
                        CityWeather.ForecastDay.ForecastDayItem.Day.Condition(
                            code = 0, icon = "", text = ""
                        )
                    )
                )
            )
        )
    )

    @Test
    fun `test success try is CityWeather`() {
        val mapper = WeatherUiMapper()
        val uiState = mapper.mapSuccess(fakeCityWeather)
        assert(uiState is WeatherUiState.Success)
    }

    @Test
    fun `test success try is CityWeatherFromDb`() {
        val mapper = WeatherUiMapper()
        val fakeListCityWeatherFromDb = listOf(
            CityWeatherFrommDb(
                name = "", current = CurrentInfo(
                    weatherState = "", weatherIcon = "", infoList = listOf()
                ),
                forecast = listOf(ItemForecast(
                    date = "", icon = "", ItemForecast.Temperature(0, 0, 0)
                ))
            )
        )

        val uiState = mapper.mapSuccess(fakeListCityWeatherFromDb)
        assert(uiState is WeatherUiState.Success)
    }

    @Test
    fun `test error try is empty CityWeatherFromDb`() {
        val mapper = WeatherUiMapper()
        val fakeListCityWeatherFromDb = listOf(
            CityWeatherFrommDb(
                name = "", current = null,
                forecast = null
            )
        )

        val uiState = mapper.mapSuccess(fakeListCityWeatherFromDb)
        assert(uiState is WeatherUiState.Error.UnknownHost)
    }

    @Test
    fun `test mapError's`() {
        val mapper = WeatherUiMapper()
        var exception: Exception
        var uiState: WeatherUiState

        exception = IllegalArgumentException()
        uiState = mapper.mapError(exception)
        assert(uiState is WeatherUiState.Error.NotFoundCity)


        exception = UnknownHostException()
        uiState = mapper.mapError(exception)
        assert(uiState is WeatherUiState.Error.UnknownHost)


        exception = SocketTimeoutException()
        uiState = mapper.mapError(exception)
        assert(uiState is WeatherUiState.Error.Timeout)


        exception = Exception()
        uiState = mapper.mapError(exception)
        assert(uiState is WeatherUiState.Error.Other)
    }
}