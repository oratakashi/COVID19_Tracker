package com.oratakashi.covid19.data.model.province

import com.google.gson.annotations.SerializedName

data class ResponseProvince (
    @SerializedName("uniqueIdField") val status : DataStatus,
    @SerializedName("features") val data : List<DataProvince>
)