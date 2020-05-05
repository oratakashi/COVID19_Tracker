package com.oratakashi.covid19.ui.home.news_module

import com.oratakashi.covid19.data.model.news.ResponseNews

sealed class NewsState {
    object Loading : NewsState()

    data class Result(val data : ResponseNews) : NewsState()
    data class Error(val error : Throwable) : NewsState()
}