package com.oratakashi.covid19.ui.timeline

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataTimeline
import com.oratakashi.covid19.data.model.timeline.ResponseTimeline
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class TimelineViewModel : ViewModel() {
    val progressTimeLine = MutableLiveData<Boolean>()
    val responseTimeLine = MutableLiveData<List<DataTimeline>>()
    val errorTimeLine = MutableLiveData<Boolean>()

    val showMessage = MutableLiveData<String>()

    fun getTimeLine(){
        progressTimeLine.value = true
        App.disposable!!.add(
            App.bnpb!!.getTimeline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseTimeline>() {
                    override fun onSuccess(t: ResponseTimeline) {
                        progressTimeLine.value = false
                        errorTimeLine.value = false
                        cacheData(t)
                        getCache()
                    }

                    override fun onError(e: Throwable) {
                        progressTimeLine.value = false
                        errorTimeLine.value = true
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
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