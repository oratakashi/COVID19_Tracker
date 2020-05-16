package com.oratakashi.covid19.ui.timeline.detail

import com.oratakashi.covid19.data.model.timeline.detail.ResponseDetailTimeline

sealed class DetailTimelineState {
    object Loading : DetailTimelineState()

    data class Result(val data : ResponseDetailTimeline) : DetailTimelineState()
    data class Error(val error : Throwable) : DetailTimelineState()
}