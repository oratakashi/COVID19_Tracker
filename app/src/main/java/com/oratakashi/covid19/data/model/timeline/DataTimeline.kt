package com.oratakashi.covid19.data.model.timeline

import com.google.gson.annotations.SerializedName

data class DataTimeline (
    @SerializedName("attributes") val attributes: DataAttributes
)