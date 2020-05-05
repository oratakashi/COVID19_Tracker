package com.oratakashi.covid19.data.model.news

import com.google.gson.annotations.SerializedName

data class DataNews (
    @SerializedName("title") val title : String?,
    @SerializedName("date") val date : String?,
    @SerializedName("description") val description : String?,
    @SerializedName("image") val image : String?,
    @SerializedName("link") val link : String?,
    @SerializedName("source") val source : String?
)