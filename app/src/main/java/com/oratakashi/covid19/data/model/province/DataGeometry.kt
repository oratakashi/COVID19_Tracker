package com.oratakashi.covid19.data.model.province

import com.google.gson.annotations.SerializedName

data class DataGeometry (
    @SerializedName("x") val lat : Float,
    @SerializedName("y") val lang : Float
)