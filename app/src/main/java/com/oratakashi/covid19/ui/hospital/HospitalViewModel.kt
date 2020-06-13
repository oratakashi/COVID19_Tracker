package com.oratakashi.covid19.ui.hospital

import android.database.Cursor
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataHospital
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HospitalViewModel @Inject constructor(
    val endpoint: ApiEndpoint
) : ViewModel(), HospitalView {

    val observer by lazy {
        MutableLiveData<HospitalState>()
    }

    val cache by lazy {
        MutableLiveData<List<DataHospital>>()
    }

    override val state: LiveData<HospitalState>
        get() = observer

    override fun getData() {
        endpoint.getHospital("RS_Rujukan_Update_May_2020")
            .map<HospitalState>(HospitalState::Result)
            .onErrorReturn(HospitalState::Error)
            .toFlowable()
            .startWith(HospitalState.Loading)
            .subscribe(observer::postValue)
            .let { App.disposable!!::add }
    }

    fun cacheData(data : List<com.oratakashi.covid19.data.model.hospital.DataHospital>){
        /**
         * Remove Old Cache
         */
        App.builder!!.delete(Database.TABLE_HOSPITAL)

        data.forEach {
            App.builder!!.insert(it)
        }

        getCache()
    }

    fun getCache(keyword : String = "", code : Int = 0){
        val db : Cursor = when(keyword.isEmpty()){
            true    -> App.builder!!.apply {
                from(Database.TABLE_HOSPITAL)
                orderBy("asc", Database.name)
                get()
                close()
            }.cursor()
            false   -> when(code){
                0    -> App.builder!!.apply {
                    from(Database.TABLE_HOSPITAL)
                    where(Database.name, keyword, "like")
                    orderBy("asc", Database.name)
                    get()
                    close()
                }.cursor()
                1   -> App.builder!!.apply {
                    from(Database.TABLE_HOSPITAL)
                    where(Database.region, keyword, "like")
                    orderBy("asc", Database.name)
                    get()
                    close()
                }.cursor()
                else -> App.builder!!.apply {
                    from(Database.TABLE_HOSPITAL)
                    where(Database.address, keyword, "like")
                    orderBy("asc", Database.name)
                    get()
                    close()
                }.cursor()
            }
        }
        db.moveToFirst()

        val data : MutableList<DataHospital> = ArrayList()

        for(i in 0 until db.count){
            db.moveToPosition(i)
            data.add(
                DataHospital(
                    db.getString(db.getColumnIndex(Database.name)),
                    db.getString(db.getColumnIndex(Database.address)),
                    db.getString(db.getColumnIndex(Database.phone)),
                    db.getString(db.getColumnIndex(Database.region)),
                    db.getString(db.getColumnIndex(Database.type)),
                    db.getFloat(db.getColumnIndex(Database.lat)),
                    db.getFloat(db.getColumnIndex(Database.long))
                )
            )
        }

        cache.value = data
    }

    fun validation(keyword: String){
        /**
         * Building AI, When User input system will check,
         * If user input a Region, Result is By Region
         * else if user input a address, Result is by Address
         * and else if user input a hospital name, Result is By Hospital Name
         */
        var db : Cursor = App.builder!!.apply {
            from(Database.TABLE_HOSPITAL)
            where(Database.region, keyword, "like")
            limit(1)
            get()
            close()
        }.cursor()

        when(db.count > 0){
            true    -> getCache(keyword, 1)
            false   -> {
                db = App.builder!!.apply {
                    from(Database.TABLE_HOSPITAL)
                    where(Database.address, keyword, "like")
                    limit(1)
                    get()
                    close()
                }.cursor()

                when(db.count > 0){
                    true    -> getCache(keyword, 2)
                    false   -> getCache(keyword, 0)
                }
            }
        }
    }

    fun observableSearch() : DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(t: TextViewTextChangeEvent) {
                val keyword = t.text.toString()
                if(keyword.trim{it <= ' '}.isNotEmpty() && keyword.trim{it <= ' '}.length > 3){
                    validation(keyword)
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

    fun checkCache() : Boolean{
        val db : Cursor = App.builder!!.apply {
            from(Database.TABLE_HOSPITAL)
            limit(1)
        }.cursor()
        db.moveToFirst()

        return db.count > 0
    }
}