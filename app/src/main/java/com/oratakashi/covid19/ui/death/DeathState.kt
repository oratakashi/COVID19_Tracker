package com.oratakashi.covid19.ui.death

import com.oratakashi.covid19.data.model.death.DataDeath

sealed class DeathState {
    object Loading : DeathState()

    data class Result(val data : List<DataDeath>) : DeathState()
    data class Error(val error : Throwable) : DeathState()
}