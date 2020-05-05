package com.oratakashi.covid19.ui.home.news_module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class NewsViewModel @Inject constructor(
    @Named("Orata") val endpoint: ApiEndpoint
) : ViewModel(), NewsView{

    val ig_observer by lazy {
        MutableLiveData<InstagramState>()
    }

    val news_observer by lazy{
        MutableLiveData<NewsState>()
    }

    override val state_ig: LiveData<InstagramState>
        get() = ig_observer

    override val state_news: LiveData<NewsState>
        get() = news_observer

    override fun getInstagram() {
        endpoint.getInstagram(10)
            .map<InstagramState>(InstagramState::Result)
            .onErrorReturn(InstagramState::Error)
            .toFlowable()
            .startWith(InstagramState.Loading)
            .observeOn(Schedulers.io())
            .subscribe(ig_observer::postValue)
            .let { App.disposable!!::add }
    }

    override fun getNews() {
        endpoint.getNews(1)
            .map<NewsState>(NewsState::Result)
            .onErrorReturn(NewsState::Error)
            .toFlowable()
            .startWith(NewsState.Loading)
            .observeOn(Schedulers.io())
            .subscribe(news_observer::postValue)
            .let { App.disposable!!::add }
    }

    override fun onCleared() {
        super.onCleared()
        App.disposable!!.clear()
    }
}