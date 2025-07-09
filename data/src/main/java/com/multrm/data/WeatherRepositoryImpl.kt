package com.multrm.data

import com.multrm.domain.Operation
import com.multrm.domain.WeatherRepository
import com.multrm.entity.CityWeather
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {
    override suspend fun getCitiesList(): List<String> {
        delay(1000)
        return citiesList
    }

    override suspend fun getWeather(cityName: String, days: Int): Operation<CityWeather> {
        return try {
            val data = api.getWeather(cityName, days.toString())
            Operation.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Operation.Failure(e)
        }
    }

    companion object {
        private val citiesList = listOf(
            "Нижний Новгород", "Москва", "Санкт-питербург", "Пермь", "Новосибирск",
            "Екатеринбург", "Казань", "Челябинск", "Самара", "Омск", "Ростов-на-Дону",
            "Уфа", "Красноярск", "Воронеж", "Волгоград", "Краснодар", "Саратов", "Тюмень",
            "Тольятти", "Ижевск", "Барнаул", "Ульяновск", "Иркутск", "Хабаровск", "Махачкала",
            "Ярославль", "Владивосток", "Оренбург", "Томск", "Кемерово"
        )
    }
}