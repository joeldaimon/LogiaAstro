package com.joguco.logiaastro.model.astrology

data class ChartRequest(
    val year: Int,
    val month: Int,
    val date: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val latitude: Double,
    val longitude: Double,
    val timezone: Double,
    val settings: Settings
)

data class Settings(
    val observation_point: String,
    val ayanamsha: String
)