package com.oratakashi.covid19.ui.province

import android.database.Cursor
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataProvince
import com.oratakashi.covid19.data.model.province.ResponseProvince
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.ui.timeline.TimelineState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProvinceViewModel @Inject constructor(
    val endpoint: ApiEndpoint
) : ViewModel(), ProvinceView {

    val cacheProvince by lazy {
        MutableLiveData<List<DataProvince>>()
    }

    val observer by lazy {
        MutableLiveData<ProvinceState>()
    }

    override val state: LiveData<ProvinceState>
        get() = observer

    override fun getData() {
        endpoint.getDataBNPB("COVID19_Indonesia_per_Provinsi")
            .map<ProvinceState>(ProvinceState::Result)
            .onErrorReturn(ProvinceState::Error)
            .toFlowable()
            .startWith(ProvinceState.Loading)
            .subscribe(observer::postValue)
            .let { App.disposable!!::add }
    }

    override fun onCleared() {
        super.onCleared()
        App.disposable!!.clear()
    }

    fun cacheData(response : ResponseProvince){
        /**
         * Remove old cache
         */

        App.builder!!.delete(Database.TABLE_PROVINCE)

        if(response.data.isNotEmpty()){
            response.data.forEach {
                if(it.attributes.provinsi != "Indonesia" && it.attributes.provinsi != null){
                    App.builder!!.insert(it)
                }
            }
        }
    }

    fun getCache(order : String = "", column : String = "", keyword : String = ""){
        val dataProvince : MutableList<DataProvince> = ArrayList()
        val db : Cursor = when(keyword.isEmpty()){
            true -> {
                App.builder!!.apply {
                    from(Database.TABLE_PROVINCE)
                    orderBy(order, column)
                    get()
                    close()
                }.cursor()
            }
            false -> {
                App.builder!!.apply {
                    from(Database.TABLE_PROVINCE)
                    orderBy(order, column)
                    where(Database.provinceState, keyword, "like")
                    get()
                    close()
                }.cursor()
            }
        }

        for(i in 0 until db.count){
            db.moveToPosition(i)
            dataProvince.add(
                DataProvince(
                    db.getString(db.getColumnIndex(Database.provinceState)),
                    db.getInt(db.getColumnIndex(Database.confirmed)),
                    db.getInt(db.getColumnIndex(Database.recovered)),
                    db.getInt(db.getColumnIndex(Database.deaths)),
                    db.getString(db.getColumnIndex(Database.lat)).toFloat(),
                    db.getString(db.getColumnIndex(Database.long)).toFloat()
                )
            )
        }

        cacheProvince.value = dataProvince

        Log.e("Jml", "Jml : ${dataProvince.size}")
    }

    fun observableSearch() : DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(t: TextViewTextChangeEvent) {
                val keyword = t.text.toString()
                if(keyword.trim{it <= ' '}.isNotEmpty() && keyword.trim{it <= ' '}.length > 3){
                    getCache("", "", keyword)
                }else{
                    getCache()
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        }
    }

    fun setupInstantSearch(editText: EditText){
        App.disposable!!.add(
            editText.textChangeEvents()
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observableSearch())
        )
    }
}