package com.oratakashi.covid19.ui.confirm

import com.oratakashi.covid19.data.model.confirm.DataConfirm

sealed class ConfirmState {
    object Loading : ConfirmState()

    data class Result(val data : List<DataConfirm>) : ConfirmState()
    data class Error(val error : Throwable) : ConfirmState()
}