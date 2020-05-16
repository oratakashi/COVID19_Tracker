package com.oratakashi.covid19.ui.timeline

import com.oratakashi.covid19.data.model.localstorage.DataTimeline

interface TimelineInterface {
    fun onSelect(data :DataTimeline)
}