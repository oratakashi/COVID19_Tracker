package com.oratakashi.covid19.ui.timeline.detail

import androidx.lifecycle.LiveData

interface DetailTimelineView {
    val state : LiveData<DetailTimelineState>

    fun getData(date : String)
}