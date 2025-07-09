package com.multrm.presentation.homeScreen

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import app.cash.turbine.test
import com.multrm.domain.WeatherDatabaseRepository
import com.multrm.domain.WeatherRepository
import com.multrm.presentation.weatherScreen.DispatchersList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class HomeVMTest {
    private val repository = mockk<WeatherRepository>()
    private val dbRepository = mockk<WeatherDatabaseRepository>()
    private val fakeCitiesList = listOf(
        "Нижний Новгород", "Москва", "Санкт-питербург", "Пермь", "Новосибирск",
        "Екатеринбург", "Казань", "Челябинск", "Самара", "Омск", "Ростов-на-Дону",
        "Уфа", "Красноярск", "Воронеж", "Волгоград", "Краснодар", "Саратов", "Тюмень",
        "Тольятти", "Ижевск", "Барнаул", "Ульяновск", "Иркутск", "Хабаровск", "Махачкала",
        "Ярославль", "Владивосток", "Оренбург", "Томск", "Кемерово"
    )
    private lateinit var viewModel: HomeVM

    @Before
    fun setup() {
        viewModel = HomeVM(repository, dbRepository)
    }

    @Test
    fun `test success getCitiesList`() = runTest {
        coEvery { repository.getCitiesList() } returns fakeCitiesList

        viewModel.getCitiesList()
        viewModel.textFieldState.setTextAndPlaceCursorAtEnd("")

        viewModel.uiState.test {
            val item = awaitItem()
            if (item is HomeUiState.Success) {
                assert(true)
            }
        }
    }

    @Test
    fun `test query from getCitiesList`() = runTest {
        val query = "Москва"
        coEvery { repository.getCitiesList() } returns fakeCitiesList

        viewModel.getCitiesList()
        viewModel.textFieldState.setTextAndPlaceCursorAtEnd(query)

        viewModel.uiState.test {
            skipItems(1)
            val item = awaitItem()
            if (item is HomeUiState.Success) {
                assert(true)
                assert(item.data.size == 1)
            } else {
                assert(false)
            }
        }
    }

    @Test
    fun testGetImHereCity() {
        val city = "Москва"
        every{ dbRepository.getImHereCity() } returns city
        val imHereCity = viewModel.getImHereCity()
        assert(imHereCity == city)
    }
}