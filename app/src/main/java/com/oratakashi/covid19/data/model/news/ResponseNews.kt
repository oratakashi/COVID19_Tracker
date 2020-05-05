package com.oratakashi.covid19.data.model.news

import com.google.gson.annotations.SerializedName

data class ResponseNews (
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : List<DataNews>
)