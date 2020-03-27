package com.oratakashi.covid19.data.model.timeline

import com.google.gson.annotations.SerializedName

data class DataAttributes (
    @SerializedName("Tanggal") val tanggal : Long?,
    @SerializedName("Jumlah_Kasus_Baru_per_Hari") val case : Int?,
    @SerializedName("Jumlah_Kasus_Dirawat_per_Hari") val confirm : Int?,
    @SerializedName("Jumlah_Kasus_Sembuh_per_Hari") val recovered : Int?,
    @SerializedName("Jumlah_Kasus_Meninggal_per_Hari") val death : Int?
)