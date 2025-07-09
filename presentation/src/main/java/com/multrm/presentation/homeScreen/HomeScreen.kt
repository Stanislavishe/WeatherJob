package com.multrm.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.multrm.presentation.LoadingState
import com.multrm.presentation.R
import com.multrm.presentation.homeScreen.components.CityCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm: HomeVM = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var isFirstCheck by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        vm.getCitiesList()
    }
    Column(modifier.padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(12.dp))
        Text(
           text = stringResource(R.string.HomeScreen_ChangeCity),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(6.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            BasicTextField(
                state = vm.textFieldState,
                modifier = Modifier.fillMaxWidth()
            )
        }
        when (val uiState = state) {
            HomeUiState.Loading -> {
                LoadingState()
            }

            is HomeUiState.Success -> {
                CitiesList(uiState, onNavigate)
            }

            else -> {
                // Это хардкод, поэтому я здесь не стал обрабатывать другие стейты
            }
        }
    }
    if (isFirstCheck) {
        isFirstCheck = false
        val imHereCity = vm.getImHereCity()
        if (imHereCity != null) {
            onNavigate(imHereCity)
        }
    }
}

@Composable
private fun CitiesList(uiState: HomeUiState.Success, onNavigate: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.data.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.HomeScreen_NotHaveCity),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 20.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(uiState.data, key = { it }) {
                CityCard(cityName = it, onClick = onNavigate)
            }
        }
    }
}