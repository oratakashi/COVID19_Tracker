package com.oratakashi.covid19.data.model.statistik

import com.google.gson.annotations.SerializedName

data class ResponseStatistik (
    @SerializedName("confirmed") val confirmed : DataConfirm?,
    @SerializedName("recovered") val recovered : DataRecovered?,
    @SerializedName("deaths") val deaths : DataDeath?
)