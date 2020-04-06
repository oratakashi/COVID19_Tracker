package com.oratakashi.covid19.ui.timeline

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataTimeline
import com.oratakashi.covid19.data.model.timeline.ResponseTimeline
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TimelineViewModel @Inject constructor(
    val endpoint: ApiEndpoint
) : ViewModel(), TimelineView {

    val responseTimeLine by lazy {
        MutableLiveData<List<DataTimeline>>()
    }

    val observer by lazy {
        MutableLiveData<TimelineState>()
    }

    override val state: LiveData<TimelineState>
        get() = observer

    override fun getData() {
        endpoint.getDataTimeline("Statistik_Perkembangan_COVID19_Indonesia")
            .map<TimelineState>(TimelineState::Result)
            .onErrorReturn(TimelineState::Error)
            .toFlowable()
            .startWith(TimelineState.Loading)
            .subscribe(observer::postValue)
            .let { App.disposable!!::add }
    }

    override fun onCleared() {
        super.onCleared()
        App.disposable!!.clear()
    }

    fun cacheData(response : ResponseTimeline){
        /**
         * Remove old cache
         */
        App.builder!!.delete(Database.TABLE_TIMELINE)

        response.data.forEach {
            if(it.attributes.confirm != null){
                App.builder!!.insert(it)
            }
        }
    }

    fun getCache(order : String = "", column : String = "", keyword : String = ""){
        val dataTimeline : MutableList<DataTimeline> = ArrayList()
        val db : Cursor = when(keyword.isEmpty()){
            true -> {
                App.builder!!.apply {
                    from(Database.TABLE_TIMELINE)
                    orderBy(order, column)
                    get()
                    close()
                }.cursor()
            }
            false -> {
                App.builder!!.apply {
                    from(Database.TABLE_TIMELINE)
                    orderBy(order, column)
                    where(Database.date, keyword)
                    get()
                    close()
                }.cursor()
            }
        }

        for(i in 0 until db.count){
            db.moveToPosition(i)
            dataTimeline.add(
                DataTimeline(
                    db.getString(db.getColumnIndex(Database.date)),
                    db.getInt(db.getColumnIndex(Database.case)),
                    db.getInt(db.getColumnIndex(Database.confirmed)),
                    db.getInt(db.getColumnIndex(Database.recovered)),
                    db.getInt(db.getColumnIndex(Database.deaths))
                )
            )
        }

        responseTimeLine.value = dataTimeline
    }
}