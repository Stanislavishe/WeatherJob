package com.multrm.entity.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.multrm.entity.weatherScreenEntity.CurrentInfo

class CurrentInfoConverter {
    private val gson = Gson()

    @TypeConverter
    fun infoToJson(info: CurrentInfo?): String? {
        return info?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun jsonToInfo(json: String?): CurrentInfo? {
        return json?.let {
            val type = object : TypeToken<CurrentInfo>(){}.type
            gson.fromJson(json, type)
        }
    }
}