package com.oratakashi.covid19.data.model.province

import com.google.gson.annotations.SerializedName

data class DataProvince (
    @SerializedName("attributes") val attributes: DataAttributes,
    @SerializedName("geometry") val geometry: DataGeometry
)