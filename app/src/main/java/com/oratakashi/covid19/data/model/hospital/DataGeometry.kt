package com.oratakashi.covid19.data.model.hospital

import com.google.gson.annotations.SerializedName

data class DataGeometry (
    @SerializedName("y") val lat : Float,
    @SerializedName("x") val lang : Float
)