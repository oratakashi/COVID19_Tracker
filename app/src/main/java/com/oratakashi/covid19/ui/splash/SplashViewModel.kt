package com.oratakashi.covid19.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.version.ResponseVersion
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    val endpoint: ApiEndpoint
) : ViewModel(), SplashView {

    private val observer = MutableLiveData<SplashState>()

    override val state: LiveData<SplashState>
        get() = observer

    override fun getData() {
        endpoint.getUpdate()
            .map<SplashState>(SplashState::Result)
            .onErrorReturn(SplashState::Error)
            .toFlowable()
            .startWith(SplashState.Loading)
            .observeOn(Schedulers.io())
            .subscribe(observer::postValue)
            .let { App.disposable!!::add }
    }

    override fun onCleared() {
        super.onCleared()
        App.disposable!!.clear()
    }
}