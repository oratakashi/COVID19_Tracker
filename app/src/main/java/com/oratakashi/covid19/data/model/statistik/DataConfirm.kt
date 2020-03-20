package com.oratakashi.covid19.data.model.statistik

import com.google.gson.annotations.SerializedName

data class DataConfirm (
    @SerializedName("value") val value : Int,
    @SerializedName("detail") val detail : String
)