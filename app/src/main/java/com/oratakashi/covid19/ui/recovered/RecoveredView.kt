package com.oratakashi.covid19.ui.recovered

import androidx.lifecycle.LiveData

interface RecoveredView {
    val state : LiveData<RecoveredState>

    fun getData()
}