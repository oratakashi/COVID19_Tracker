package com.oratakashi.covid19.ui.hotline

import android.content.Context
import android.database.Cursor
import android.widget.EditText
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataHotline
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.utils.AssetsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import java.util.concurrent.TimeUnit

class HotlineViewModel : ViewModel() {
    val cache = MediatorLiveData<List<DataHotline>>()

    @Throws(JSONException::class)
    fun getHotline(context : Context) : List<com.oratakashi.covid19.data.model.hotline.DataHotline>{
        val data : MutableList<com.oratakashi.covid19.data.model.hotline.DataHotline> = ArrayList()

        for(i in 0 until AssetsManager.getJson(context, "hotline").length()){
            val json = AssetsManager.getJson(context, "hotline").getJSONObject(i)
            data.add(
                com.oratakashi.covid19.data.model.hotline.DataHotline(
                    json.getString("province"),
                    json.getString("phone")
                )
            )
        }

        return data
    }

    fun checkCache() : Boolean {
        val db : Cursor = App.builder!!.apply {
            from(Database.TABLE_HOTLINE)
            limit(1)
            get()
            close()
        }.cursor()

        return db.count < 1
    }

    fun cacheData(data : List<com.oratakashi.covid19.data.model.hotline.DataHotline>){
        /**
         * Clear Old Cache
         */
        App.builder!!.delete(Database.TABLE_HOTLINE)

        data.forEach {
            App.builder!!.insert(it)
        }

        getCache()
    }

    fun getCache(keyword : String = ""){
        val data : MutableList<DataHotline> = ArrayList()
        val db : Cursor = when(keyword.isEmpty()){
            true    -> App.builder!!.apply {
                from(Database.TABLE_HOTLINE)
                orderBy("asc", Database.province)
                get()
                close()
            }.cursor()
            false   -> App.builder!!.apply {
                from(Database.TABLE_HOTLINE)
                where(Database.province, keyword, "like")
                orderBy("asc", Database.province)
                get()
                close()
            }.cursor()
        }
        for(i in 0 until db.count){
            db.moveToPosition(i)
            data.add(
                DataHotline(
                    db.getString(db.getColumnIndex(Database.province)),
                    db.getString(db.getColumnIndex(Database.phone))
                )
            )
        }

        cache.value = data
    }

    fun observableSearch() : DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(t: TextViewTextChangeEvent) {
                val keyword = t.text.toString()
                if(keyword.trim{it <= ' '}.isNotEmpty() && keyword.trim{it <= ' '}.length > 3){
                    getCache(keyword)
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