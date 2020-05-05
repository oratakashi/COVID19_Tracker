package com.oratakashi.covid19.ui.home.global_module

import com.oratakashi.covid19.data.model.statistik.ResponseStatistik

sealed class GlobalState {
    object Loading : GlobalState()

    data class Result(val data : ResponseStatistik) : GlobalState()
    data class Error(val error : Throwable) : GlobalState()
}