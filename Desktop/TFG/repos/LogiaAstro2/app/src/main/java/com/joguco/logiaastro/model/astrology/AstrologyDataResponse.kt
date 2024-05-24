package com.joguco.logiaastro.model.astrology

import com.google.gson.annotations.SerializedName

data class AstrologyDataResponse(
    @SerializedName("output") val chartData: List<NatalChart>
)
