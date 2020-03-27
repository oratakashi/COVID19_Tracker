package com.oratakashi.covid19.data.model.timeline

import com.google.gson.annotations.SerializedName

data class ResponseTimeline (
    @SerializedName("features") val data : List<DataTimeline>
)