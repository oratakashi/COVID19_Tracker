package com.oratakashi.covid19.ui.home

import androidx.lifecycle.LiveData

interface LocalView {
    val state : LiveData<LocalState>

    fun getData()
}