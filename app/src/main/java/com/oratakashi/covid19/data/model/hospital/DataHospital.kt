package com.oratakashi.covid19.data.model.hospital

import com.google.gson.annotations.SerializedName


data class DataHospital (
    @SerializedName("attributes") val attributes: DataAttributes,
    @SerializedName("geometry") val geometry: DataGeometry
)