package com.oratakashi.covid19.ui.home.global_module

import androidx.lifecycle.LiveData
import com.oratakashi.covid19.ui.home.global_module.GlobalState

interface GlobalView {
    val state : LiveData<GlobalState>

    fun getData()
}