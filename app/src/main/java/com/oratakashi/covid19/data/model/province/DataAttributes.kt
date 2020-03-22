package com.oratakashi.covid19.data.model.province

import com.google.gson.annotations.SerializedName

data class DataAttributes (
    @SerializedName("Provinsi") val provinsi : String?,
    @SerializedName("Kasus_Terkonfirmasi_Akumulatif") val confirm : Int?,
    @SerializedName("Kasus_Sembuh_Akumulatif") val recovered : Int?,
    @SerializedName("Kasus_Meninggal_Akumulatif") val death : Int?,
    @SerializedName("Pembaruan") val updates : String?
)