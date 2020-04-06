package com.oratakashi.covid19.ui.statistik

import androidx.lifecycle.LiveData

interface StatistikView {
    val state : LiveData<StatistikState>

    fun getData()
}