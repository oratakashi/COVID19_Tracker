package com.oratakashi.covid19.data.model.instagram

import com.google.gson.annotations.SerializedName

data class DataInstagram (
    @SerializedName("username") val username : String?,
    @SerializedName("user_photo") val user_photo : String?,
    @SerializedName("user_followers") val user_followers : Int?,
    @SerializedName("user_media") val media : List<DataMedia>?
)