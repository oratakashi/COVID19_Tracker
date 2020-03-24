package com.oratakashi.covid19.ui.recovered

import android.database.Cursor
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataGlobal
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RecoveredViewModel : ViewModel() {
    val progressRecovered = MutableLiveData<Boolean>()
    val responseRecovered = MutableLiveData<List<DataRecovered>>()
    val errorRecovered = MutableLiveData<Boolean>()

    val cacheRecovered = MutableLiveData<List<DataGlobal>>()

    val showMessage = MutableLiveData<String>()

    fun getRecovered(){
        progressRecovered.value = true
        App.disposable!!.add(
            App.service!!.getRecovered()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DataRecovered>>() {
                    override fun onSuccess(t: List<DataRecovered>) {
                        progressRecovered.value = false
                        errorRecovered.value = false
                        responseRecovered.value = t

                        cacheData(t)
                        getCache()
                    }

                    override fun onError(e: Throwable) {
                        progressRecovered.value = false
                        errorRecovered.value = true
                        getCache()
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
    }

    fun cacheData(data : List<DataRecovered>){
        /**
         * Remove old cache
         */

        App.builder!!.delete(Database.TABLE_RECOVERED)

        if(data.isNotEmpty()){
            data.forEach {
                App.builder!!.insert(it)
            }
        }
    }

    fun getCache(order : String = "", column : String = "", keyword : String = ""){
        val dataGlobal : MutableList<DataGlobal> = ArrayList()
        val db : Cursor = when(keyword.isEmpty()){
            true -> {
                App.builder!!.apply {
                    from(Database.TABLE_RECOVERED)
                    orderBy(order, column)
                    get()
                    close()
                }.cursor()
            }
            false -> {
                App.builder!!.apply {
                    from(Database.TABLE_RECOVERED)
                    orderBy(order, column)
                    where(Database.countryRegion, keyword, "like")
                    get()
                    close()
                }.cursor()
            }
        }

        for(i in 0 until db.count){
            db.moveToPosition(i)
            dataGlobal.add(
                DataGlobal(
                    db.getString(db.getColumnIndex(Database.provinceState)),
                    db.getString(db.getColumnIndex(Database.countryRegion)),
                    db.getString(db.getColumnIndex(Database.lat)).toFloat(),
                    db.getString(db.getColumnIndex(Database.long)).toFloat(),
                    db.getInt(db.getColumnIndex(Database.confirmed)),
                    db.getInt(db.getColumnIndex(Database.recovered)),
                    db.getInt(db.getColumnIndex(Database.deaths))
                )
            )
        }

        cacheRecovered.value = dataGlobal
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