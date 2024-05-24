package com.joguco.logiaastro.tabs.astrology

import com.google.gson.annotations.SerializedName
import com.joguco.logiaastro.model.NatalChart

data class AstrologyDataResponse(
    @SerializedName("output") val chartData: List<NatalChart>
)
