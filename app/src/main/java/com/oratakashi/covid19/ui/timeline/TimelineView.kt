package com.oratakashi.covid19.ui.timeline

import androidx.lifecycle.LiveData

interface TimelineView {
    val state : LiveData<TimelineState>

    fun getData()
}