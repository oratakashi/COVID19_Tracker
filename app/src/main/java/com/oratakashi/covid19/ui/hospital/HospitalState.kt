package com.oratakashi.covid19.ui.hospital

import com.oratakashi.covid19.data.model.hospital.ResponseHospital

sealed class HospitalState {
    object Loading : HospitalState()

    data class Result(val data : ResponseHospital) : HospitalState()
    data class Error(val error : Throwable) : HospitalState()
}