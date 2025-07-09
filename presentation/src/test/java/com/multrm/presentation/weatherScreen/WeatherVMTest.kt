package com.multrm.presentation.weatherScreen

import app.cash.turbine.test
import com.multrm.domain.Operation
import com.multrm.domain.WeatherDatabaseRepository
import com.multrm.domain.WeatherRepository
import com.multrm.entity.CityWeather
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherVMTest {
    private val repository = mockk<WeatherRepository>()
    private val dbRepository = mockk<WeatherDatabaseRepository>()
    private val mapper = WeatherUiMapper()
    private lateinit var viewModel: WeatherVM

    @Before
    fun setup() {
        viewModel = WeatherVM(repository, dbRepository, mapper, TestDispatchersList())
    }

    @Test
    fun `test success getWeather try have internet`() = runTest {
        coEvery {
            repository.getWeather(any(), any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Success>()

        viewModel.getWeather("")

        viewModel.uiState.test {
            assert(awaitItem() is WeatherUiState.Success)
        }
    }

    @Test
    fun `test success getWeather try have internet but not active VPN`() = runTest {
        coEvery {
            repository.getWeather(any(), any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Error.Timeout>()

        viewModel.getWeather("")

        viewModel.uiState.test {
            assert(awaitItem() is WeatherUiState.Error.Timeout)
        }
    }

    @Test
    fun `test success getWeather try have internet but city not found`() = runTest {
        coEvery {
            repository.getWeather(any(), any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Error.NotFoundCity>()

        viewModel.getWeather("")

        viewModel.uiState.test {
            assert(awaitItem() is WeatherUiState.Error.NotFoundCity)
        }
    }

    @Test
    fun `test success getWeather try other errors`() = runTest {
        coEvery {
            repository.getWeather(any(), any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Error.Other>()

        viewModel.getWeather("")

        viewModel.uiState.test {
            assert(awaitItem() is WeatherUiState.Error.Other)
        }
    }

    @Test
    fun `test success getWeather try not have internet and not have cache`() = runTest {
        coEvery {
            repository.getWeather(any(), any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Error.UnknownHost>()
        coEvery {
            dbRepository.getCacheWeather(any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Error.UnknownHost>()

        viewModel.getWeather("")

        viewModel.uiState.test {
            assert(awaitItem() is WeatherUiState.Error.UnknownHost)
        }
    }

    @Test
    fun `test success getWeather try not have internet and have cache`() = runTest {
        coEvery {
            repository.getWeather(any(), any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Error.UnknownHost>()
        coEvery {
            dbRepository.getCacheWeather(any()).map(any(Operation.Mapper::class))
        } returns mockk<WeatherUiState.Success>()

        viewModel.getWeather("")

        viewModel.uiState.test {
            assert(awaitItem() is WeatherUiState.Success)
        }
    }

    private class TestDispatchersList(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
    ) : DispatchersList {
        override fun io(): CoroutineDispatcher = dispatcher

        override fun ui(): CoroutineDispatcher = dispatcher
    }
}