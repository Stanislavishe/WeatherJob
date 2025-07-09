package com.multrm.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.multrm.presentation.Routes
import com.multrm.presentation.homeScreen.HomeScreen
import com.multrm.presentation.weatherScreen.WeatherScreen

@Composable
fun WeatherNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen,
        modifier = Modifier
            .padding(innerPadding)
    ) {
        composable<Routes.HomeScreen> {
            HomeScreen { city ->
                navController.navigate(Routes.WeatherScreen(city))
            }
        }
        composable<Routes.WeatherScreen> {
            val city = it.toRoute<Routes.WeatherScreen>().city
            WeatherScreen(
                cityName = city
            )
        }
    }
}