package com.multrm.presentation.weatherScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.multrm.presentation.LoadingState
import com.multrm.presentation.R
import com.multrm.presentation.weatherScreen.components.ErrorBox
import com.multrm.presentation.weatherScreen.components.ItemForecastInfo
import com.multrm.presentation.weatherScreen.components.ItemWeatherInfo
import com.multrm.presentation.weatherScreen.components.WeatherIcon

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    cityName: String,
    vm: WeatherVM = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        vm.getWeather(cityName)
    }
    var isImHere by remember { mutableStateOf(false) }
    isImHere = vm.getImHereCity() == cityName
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            stringResource(R.string.WeatherScreen_Title),
            fontSize = 32.sp
        )
        Text(
            cityName,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        when (val state = uiState) {
            WeatherUiState.Error.NotFoundCity -> {
                ErrorBox(
                    errorText = "Не удалось найти погоду здесь(",
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { vm.getWeather(cityName) }
            }

            WeatherUiState.Error.Other -> {
                ErrorBox(
                    errorText = "Что-то пошло не так", Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { vm.getWeather(cityName) }
            }

            WeatherUiState.Error.UnknownHost -> {
                ErrorBox(
                    errorText = "Похоже у вас проблемы с интернетом",
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { vm.getWeather(cityName) }
            }

            WeatherUiState.Error.Timeout -> {
                ErrorBox(
                    errorText = "Похоже у вас медленный интернет или не включен VPN)",
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { vm.getWeather(cityName) }
            }

            WeatherUiState.Initial -> {}
            WeatherUiState.Loading -> {
                LoadingState()
            }

            is WeatherUiState.Success -> {
                WeatherContent(state)
                Spacer(Modifier.height(8.dp))
                if (isImHere) {
                    Button(onClick = {
                        vm.setImHere(null)
                        isImHere = !isImHere
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Я тут",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    OutlinedButton(onClick = {
                        vm.setImHere(cityName)
                        isImHere = !isImHere
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Я тут",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(state: WeatherUiState.Success) {
    Column(Modifier.heightIn(max = 5000.dp)) {
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.current.weatherState,
                fontSize = 28.sp
            )
            WeatherIcon(
                Modifier
                    .padding(start = 4.dp)
                    .size(25.dp), state.current.weatherIcon
            )
        }
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            val gridList = state.current.infoList.toMutableList()
            gridList.removeAt(gridList.lastIndex)
            items(gridList, key = { it.title }) { item ->
                ItemWeatherInfo(item)
            }
        }
        Spacer(Modifier.height(6.dp))
        ItemWeatherInfo(state.current.infoList.last())
        Spacer(Modifier.height(12.dp))
        Text(
            "Прогноз на несколько дней",
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.forecast) {
                ItemForecastInfo(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenPreview() {
//    WeatherScreen()
}