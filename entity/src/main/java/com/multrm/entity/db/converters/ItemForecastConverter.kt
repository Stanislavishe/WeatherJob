package com.multrm.entity.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.multrm.entity.weatherScreenEntity.CurrentInfo
import com.multrm.entity.weatherScreenEntity.ItemForecast

class ItemForecastConverter {
    private val gson = Gson()

    @TypeConverter
    fun listToJson(list: List<ItemForecast>?): String? {
        return list?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun jsonToList(json: String?): List<ItemForecast>? {
        return json?.let {
            val type = object : TypeToken<List<ItemForecast>>(){}.type
            gson.fromJson(json, type)
        }
    }
}