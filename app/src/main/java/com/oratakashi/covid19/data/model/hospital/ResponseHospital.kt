package com.oratakashi.covid19.data.model.hospital

import com.google.gson.annotations.SerializedName

data class ResponseHospital (
    @SerializedName("features") val data : List<DataHospital>
)