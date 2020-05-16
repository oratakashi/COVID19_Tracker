package com.oratakashi.covid19.data.model.timeline.detail

import com.google.gson.annotations.SerializedName

data class DataDetailTimeline (
    @SerializedName("attributes") val attributes: DataAttributes
)