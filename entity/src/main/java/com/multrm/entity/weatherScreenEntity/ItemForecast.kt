package com.multrm.entity.weatherScreenEntity

data class ItemForecast(
    val date: String,
    val icon: String,
    val temp: Temperature
) {
    data class Temperature(
        val resId: Int,
        val minTemp: Int,
        val maxTemp: Int
    )
}
