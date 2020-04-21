package com.oratakashi.covid19.ui.home

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataProvince
import com.oratakashi.covid19.data.model.localstorage.dashboard.DataLocal
import com.oratakashi.covid19.data.model.province.ResponseProvince
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.root.App
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class LocalViewModel @Inject constructor(
    @Named("bnpb") val endpoint: ApiEndpoint
) : ViewModel(), LocalView {

    val observer by lazy {
        MutableLiveData<LocalState>()
    }

    override val state: LiveData<LocalState>
        get() = observer

    override fun getData() {
        endpoint.getDataBNPB("COVID19_Indonesia_per_Provinsi")
            .map<LocalState>(LocalState::Result)
            .onErrorReturn(LocalState::Error)
            .toFlowable()
            .startWith(LocalState.Loading)
            .observeOn(Schedulers.io())
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

    fun countData(province : String = "") : DataLocal{
        val db : Cursor = when(province.isEmpty()){
            true -> {
                App.builder!!.apply {
                    from(Database.TABLE_PROVINCE)
                    get()
                    close()
                }.cursor()
            }
            false -> {
                App.builder!!.apply {
                    from(Database.TABLE_PROVINCE)
                    where(Database.provinceState, province)
                    get()
                    close()
                }.cursor()
            }
        }
        db.moveToFirst()

        /**
         * Prepare for return
         */
        var confirmed = 0
        var recovered = 0
        var death = 0

        for(i in 0 until db.count){
            db.moveToPosition(i)
            confirmed += db.getInt(db.getColumnIndex(Database.confirmed))
            recovered += db.getInt(db.getColumnIndex(Database.recovered))
            death += db.getInt(db.getColumnIndex(Database.deaths))
        }

        val currentTime: String =
            SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("in", "ID")).format(Date())

        return DataLocal(
            confirmed,
            recovered,
            death,
            currentTime
        )
    }
}