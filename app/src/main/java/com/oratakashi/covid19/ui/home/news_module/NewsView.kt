package com.oratakashi.covid19.ui.home.news_module

import androidx.lifecycle.LiveData

interface NewsView {
    val state_ig : LiveData<InstagramState>
    val state_news : LiveData<NewsState>

    fun getInstagram()
    fun getNews()
}