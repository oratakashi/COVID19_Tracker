package com.oratakashi.covid19.data.model.instagram

import com.google.gson.annotations.SerializedName

data class DataMedia (
    @SerializedName("link") val link : String?,
    @SerializedName("thumbs") val thumbs : String?,
    @SerializedName("date") val date : Long?,
    @SerializedName("caption") val caption : String?,
    @SerializedName("likes") val likes : Int?,
    @SerializedName("comments") val comments : Int?,
    @SerializedName("username") val username : String?,
    @SerializedName("user_photo") val user_photo : String?
)