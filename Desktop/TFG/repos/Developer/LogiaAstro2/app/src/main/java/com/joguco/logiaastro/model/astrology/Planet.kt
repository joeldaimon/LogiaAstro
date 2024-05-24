package com.joguco.logiaastro.model.astrology

import com.google.gson.annotations.SerializedName

data class Planet(
    @SerializedName("name") val name: String,
    @SerializedName("fullDegree") val degree: Double,
    @SerializedName("normDegree") val normDegree: Double,
    @SerializedName("isRetro") val isRetro: String,
    @SerializedName("current_sign") val sign: Int
)