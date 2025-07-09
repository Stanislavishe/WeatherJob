package com.multrm.entity.weatherScreenEntity

data class CurrentInfo(
    val weatherState: String,
    val weatherIcon: String,
    val infoList: List<ItemInfo>
)
