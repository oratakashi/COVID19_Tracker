package com.oratakashi.covid19.ui.splash

import androidx.lifecycle.LiveData

interface SplashView {
    val state : LiveData<SplashState>

    fun getData()
}