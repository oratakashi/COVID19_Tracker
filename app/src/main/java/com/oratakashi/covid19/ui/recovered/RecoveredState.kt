package com.oratakashi.covid19.ui.recovered

import com.oratakashi.covid19.data.model.recovered.DataRecovered

sealed class RecoveredState {
    object Loading : RecoveredState()

    data class Result(val data : List<DataRecovered>) : RecoveredState()
    data class Error(val error : Throwable) : RecoveredState()
}