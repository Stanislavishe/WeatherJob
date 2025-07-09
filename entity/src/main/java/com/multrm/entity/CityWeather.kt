package com.multrm.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.management.monitor.StringMonitor

@JsonClass(generateAdapter = true)
data class CityWeather(
    val location: Location,
    val current: Current,
    val forecast: ForecastDay
) {
    @JsonClass(generateAdapter = true)
    data class Location(
        val country: String,
        val lat: Double,
        val localtime: String,
        @Json(name = "localtime_epoch") val localtimeEpoch: Int,
        val lon: Double,
        val name: String,
        val region: String,
        @Json(name = "tz_id") val tzId: String
    )

    @JsonClass(generateAdapter = true)
    data class Current(
        val condition: Condition,
        @Json(name = "feelslike_c") val feelsLikeC: Double,
        @Json(name = "gust_kph") val gustKph: Double,
        val humidity: Int,
        @Json(name = "last_updated_epoch") val lastUpdatedEpoch: Int,
        @Json(name = "pressure_mb") val pressureMb: Double,
        @Json(name = "temp_c") val tempC: Double,
        val uv: Double,
        @Json(name = "wind_kph") val winKph: Double,
        @Json(name = "wind_dir") val windDir: String,
        @Json(name = "dewpoint_c") val dewPointC: Double
    ) {
        @JsonClass(generateAdapter = true)
        data class Condition(
            val code: Int,
            val icon: String,
            val text: String
        )
    }

    @JsonClass(generateAdapter = true)
    data class ForecastDay(
        val forecastday: List<ForecastDayItem>
    ){
        @JsonClass(generateAdapter = true)
        data class ForecastDayItem(
            val date: String,
            val day: Day
        ) {
            @JsonClass(generateAdapter = true)
            data class Day(
                @Json(name = "maxtemp_c") val maxTemp: Double,
                @Json(name = "mintemp_c") val minTemp: Double,
                val condition: Condition
            ) {
                @JsonClass(generateAdapter = true)
                data class Condition(
                    val code: Int,
                    val icon: String,
                    val text: String
                )
            }
        }
    }
}