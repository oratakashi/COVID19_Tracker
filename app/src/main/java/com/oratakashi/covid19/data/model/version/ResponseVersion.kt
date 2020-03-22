package com.oratakashi.covid19.data.model.version

import com.google.gson.annotations.SerializedName

data class ResponseVersion (
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : DataVersion
)