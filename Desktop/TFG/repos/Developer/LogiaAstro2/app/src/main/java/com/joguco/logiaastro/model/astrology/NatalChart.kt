package com.joguco.logiaastro.model.astrology

import com.google.gson.annotations.SerializedName

data class NatalChart(
    @SerializedName("0") val ascendant: Planet,
    @SerializedName("1") val sun: Planet,
    @SerializedName("2") val moon: Planet,
    @SerializedName("3") val mars: Planet,
    @SerializedName("4") val mercury: Planet,
    @SerializedName("5") val jupiter: Planet,
    @SerializedName("6") val venus: Planet,
    @SerializedName("7") val saturn: Planet,
    @SerializedName("8") val rahu: Planet,
    @SerializedName("9") val ketu: Planet,
    @SerializedName("10") val uranus: Planet,
    @SerializedName("11") val neptune: Planet,
    @SerializedName("12") val pluto: Planet
)

