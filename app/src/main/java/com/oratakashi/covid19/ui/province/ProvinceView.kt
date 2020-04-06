package com.oratakashi.covid19.ui.province

import androidx.lifecycle.LiveData

interface ProvinceView {
    val state : LiveData<ProvinceState>

    fun getData()
}