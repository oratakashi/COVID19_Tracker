package com.oratakashi.covid19.ui.home.dialog

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.dashboard.DataLocal
import com.oratakashi.covid19.root.App
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeDialogViewModel : ViewModel() {
    val count = MutableLiveData<DataLocal>()
    val dataProvince = MutableLiveData<List<DataLocal>>()

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

        if(province.isEmpty()) count.value = DataLocal(
            confirmed,
            recovered,
            death,
            currentTime
        )

        return when(province.isEmpty()){
            true -> {
                DataLocal(
                    confirmed,
                    recovered,
                    death,
                    currentTime
                )
            }
            false -> {
                DataLocal(
                    confirmed,
                    recovered,
                    death,
                    currentTime,
                    db.getString(db.getColumnIndex(Database.provinceState))
                )
            }
        }
    }

    fun getData(){
        val data : MutableList<DataLocal> = ArrayList()

        val db : Cursor = App.builder!!.apply {
            from(Database.TABLE_PROVINCE)
            get()
            close()
        }.cursor()

        for(i in 0 until db.count) {
            db.moveToPosition(i)
            data.add(countData(
                db.getString(db.getColumnIndex(Database.provinceState))
            ))
        }

        dataProvince.value = data
    }
}