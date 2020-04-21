package com.oratakashi.covid19.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class GlobalViewModel @Inject constructor(
    @Named("global") val endpoint: ApiEndpoint
) : ViewModel(), GlobalView {
    val observer by lazy{
        MutableLiveData<GlobalState>()
    }

    override val state: LiveData<GlobalState>
        get() = observer

    override fun getData() {
        endpoint.getData()
            .map<GlobalState>(GlobalState::Result)
            .onErrorReturn(GlobalState::Error)
            .toFlowable()
            .startWith(GlobalState.Loading)
            .observeOn(Schedulers.io())
            .subscribe(observer::postValue)
            .let{ App.disposable!!::add }
    }
}