package com.oratakashi.covid19.ui.death

import androidx.lifecycle.LiveData

interface DeathView {
    val state : LiveData<DeathState>

    fun getData()
}