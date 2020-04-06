package com.oratakashi.covid19.ui.splash

import com.oratakashi.covid19.data.model.version.ResponseVersion

sealed class SplashState {
    object Loading : SplashState()

    data class Result(val data : ResponseVersion) : SplashState()
    data class Error(val error : Throwable) : SplashState()
}