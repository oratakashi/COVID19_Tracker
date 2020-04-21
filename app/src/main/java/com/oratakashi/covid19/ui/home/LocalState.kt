package com.oratakashi.covid19.ui.home

import com.oratakashi.covid19.data.model.province.ResponseProvince

sealed class LocalState {
    object Loading : LocalState()

    data class Result(val data : ResponseProvince) : LocalState()
    data class Error(val error : Throwable) : LocalState()
}