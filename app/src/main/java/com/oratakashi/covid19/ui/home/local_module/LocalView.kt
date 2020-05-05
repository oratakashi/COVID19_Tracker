package com.oratakashi.covid19.ui.home.local_module

import androidx.lifecycle.LiveData
import com.oratakashi.covid19.ui.home.local_module.LocalState

interface LocalView {
    val state : LiveData<LocalState>

    fun getData()
}