package com.oratakashi.covid19.data.model.version

import com.google.gson.annotations.SerializedName

data class DataVersion (
    @SerializedName("id_version") val id_version : String,
    @SerializedName("version") val version : String,
    @SerializedName("version_char") val version_char : String,
    @SerializedName("changelog") val changelog : String,
    @SerializedName("link_official") val link_official : String,
    @SerializedName("link_mirror") val link_mirror : String,
    @SerializedName("updated_at") val updated_at : String
)