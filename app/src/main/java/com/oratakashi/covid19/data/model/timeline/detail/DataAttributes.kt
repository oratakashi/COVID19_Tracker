package com.oratakashi.covid19.data.model.timeline.detail

import com.google.gson.annotations.SerializedName

data class DataAttributes (
    @SerializedName("Tanggal") val tanggal : Long?,
    @SerializedName("Provinsi") val Provinsi : String?,
    @SerializedName("Penambahan_Harian_Kasus_Terkonf") val confirm : Int?,
    @SerializedName("Penambahan_Harian_Kasus_Sembuh") val recovered : Int?,
    @SerializedName("Penambahan_Harian_Kasus_Meningg") val death : Int?
)