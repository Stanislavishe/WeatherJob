package com.multrm.presentation.weatherScreen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multrm.entity.weatherScreenEntity.ItemInfo
import com.multrm.presentation.R

@Composable
fun ItemWeatherInfo(info: ItemInfo, isLast: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .padding(12.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(info.titleResId),
            fontSize = 20.sp,
            color = Color.DarkGray
        )
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = if (isLast) stringResource(info.value) else info.value.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                softWrap = true,
            )
            Spacer(Modifier.width(4.dp))
            val measurementResId = info.measurementResId
            Text(
                text = if (measurementResId != 0) stringResource(measurementResId) else "" ,
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemWeatherInfoPreview() {
    ItemWeatherInfo(ItemInfo(R.string.temperature, 35, R.string.c), isLast = false)
}