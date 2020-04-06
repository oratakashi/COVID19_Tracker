package com.oratakashi.covid19.ui.timeline

import com.oratakashi.covid19.data.model.timeline.ResponseTimeline

sealed class TimelineState {
    object Loading : TimelineState()

    data class Result(val data : ResponseTimeline) : TimelineState()
    data class Error(val error : Throwable) : TimelineState()
}