package com.oratakashi.covid19.ui.confirm

import androidx.lifecycle.LiveData

interface ConfirmView {
    val state : LiveData<ConfirmState>

    fun getData()
}