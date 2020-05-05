package com.oratakashi.covid19.data.model.instagram

import com.google.gson.annotations.SerializedName

data class ResponseInstagram (
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : DataInstagram
)