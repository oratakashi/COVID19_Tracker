package com.oratakashi.covid19.data.model.hospital

import com.google.gson.annotations.SerializedName

data class DataAttributes (
    @SerializedName("nama") val nama : String?,
    @SerializedName("alamat") val alamat : String?,
    @SerializedName("telepon") val telepon : String?,
    @SerializedName("wilayah") val wilayah : String?,
    @SerializedName("tipe") val tipe : String?
)