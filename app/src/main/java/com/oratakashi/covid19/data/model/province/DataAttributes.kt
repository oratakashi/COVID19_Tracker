package com.oratakashi.covid19.data.model.province

import com.google.gson.annotations.SerializedName

data class DataAttributes (
    @SerializedName("Provinsi") val provinsi : String?,
    @SerializedName("Kasus_Posi") val confirm : Int?,
    @SerializedName("Kasus_Semb") val recovered : Int?,
    @SerializedName("Kasus_Meni") val death : Int?,
    @SerializedName("Pembaruan") val updates : String?
)