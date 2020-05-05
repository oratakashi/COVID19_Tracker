package com.oratakashi.covid19.ui.home.news_module

import com.oratakashi.covid19.data.model.instagram.ResponseInstagram

sealed class InstagramState {
    object Loading : InstagramState()

    data class Result(val data : ResponseInstagram) : InstagramState()
    data class Error(val error : Throwable) : InstagramState()
}