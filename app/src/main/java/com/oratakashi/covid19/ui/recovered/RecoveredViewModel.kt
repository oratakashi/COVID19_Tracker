package com.oratakashi.covid19.ui.recovered

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class RecoveredViewModel : ViewModel() {
    val progressRecovered = MutableLiveData<Boolean>()
    val responseRecovered = MutableLiveData<List<DataRecovered>>()
    val errorRecovered = MutableLiveData<Boolean>()

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
                    }

                    override fun onError(e: Throwable) {
                        progressRecovered.value = false
                        errorRecovered.value = true
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
    }
}