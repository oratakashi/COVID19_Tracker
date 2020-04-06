package com.oratakashi.covid19.ui.statistik

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StatistikViewModel @Inject constructor(
    val endpoint: ApiEndpoint
) : ViewModel(), StatistikView {

    val observer by lazy {
        MutableLiveData<StatistikState>()
    }

    override val state: LiveData<StatistikState>
        get() = observer

    override fun getData() {
        endpoint.getData()
            .map<StatistikState>(StatistikState::Result)
            .onErrorReturn(StatistikState::Error)
            .toFlowable()
            .startWith(StatistikState.Loading)
            .subscribe(observer::postValue)
            .let { App.disposable!!::add }
    }

    override fun onCleared() {
        super.onCleared()
        App.disposable!!.clear()
    }
}