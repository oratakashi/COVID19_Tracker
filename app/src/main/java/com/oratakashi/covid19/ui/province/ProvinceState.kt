package com.oratakashi.covid19.ui.province

import com.oratakashi.covid19.data.model.province.ResponseProvince

sealed class ProvinceState {
    object Loading : ProvinceState()

    data class Result(val data : ResponseProvince) : ProvinceState()
    data class Error(val error : Throwable) : ProvinceState()
}