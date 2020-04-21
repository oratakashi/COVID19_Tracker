package com.oratakashi.covid19.ui.home

import androidx.lifecycle.LiveData

interface GlobalView {
    val state : LiveData<GlobalState>

    fun getData()
}