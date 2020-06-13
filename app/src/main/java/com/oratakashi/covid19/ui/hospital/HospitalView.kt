package com.oratakashi.covid19.ui.hospital

import androidx.lifecycle.LiveData

interface HospitalView {
    val state : LiveData<HospitalState>

    fun getData()
}