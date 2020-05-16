package com.oratakashi.covid19.ui.timeline.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import javax.inject.Inject

class DetailTimelineViewModel @Inject constructor(
    val endpoint: ApiEndpoint
) : ViewModel(), DetailTimelineView {

    val observer by lazy {
        MutableLiveData<DetailTimelineState>()
    }

    override val state: LiveData<DetailTimelineState>
        get() = observer

    override fun getData(date: String) {
        endpoint.getDetailTimeline(
            "Statistik_Harian_per_Provinsi_COVID19_Indonesia_Rev",
            "Tanggal >= TIMESTAMP '$date 00:00:00'"
        )
            .map<DetailTimelineState>(DetailTimelineState::Result)
            .onErrorReturn(DetailTimelineState::Error)
            .toFlowable()
            .startWith(DetailTimelineState.Loading)
            .subscribe(observer::postValue)
            .let { App.disposable!!::add }
    }
}