package com.oratakashi.covid19.ui.statistik

import com.oratakashi.covid19.data.model.statistik.ResponseStatistik

sealed class StatistikState {
    object Loading : StatistikState()

    data class Result(val data : ResponseStatistik) : StatistikState()
    data class Error(val error : Throwable) : StatistikState()
}