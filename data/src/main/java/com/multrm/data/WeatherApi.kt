package com.multrm.data

import com.multrm.entity.CityWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json?key=25ea3a217a4741d299c100631250707&lang=ru")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("days") daysCount: String
    ): CityWeather
}