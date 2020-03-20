package com.oratakashi.covid19.data.model.confirm

import com.google.gson.annotations.SerializedName

data class DataConfirm (
    @SerializedName("provinceState") val provinceState : String?,
    @SerializedName("countryRegion") val countryRegion : String?,
    @SerializedName("lat") val lat : Float?,
    @SerializedName("long") val long : Float?,
    @SerializedName("confirmed") val confirmed : Int?,
    @SerializedName("recovered") val recovered : Int?,
    @SerializedName("deaths") val deaths : Int?,
    @SerializedName("active") val active : Int?
)