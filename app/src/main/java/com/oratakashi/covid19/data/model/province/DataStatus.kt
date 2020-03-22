package com.oratakashi.covid19.data.model.province

import com.google.gson.annotations.SerializedName

data class DataStatus (
    @SerializedName("isSystemMaintained") val isMaintence : Boolean
)