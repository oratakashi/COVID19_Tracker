package com.oratakashi.covid19.ui.home

import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import java.util.*

sealed class GlobalState {
    object Loading : GlobalState()

    data class Result(val data : ResponseStatistik) : GlobalState()
    data class Error(val error : Throwable) : GlobalState()
}